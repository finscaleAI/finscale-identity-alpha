package org.muellners.finscale.identity.repository

import java.time.Instant
import org.muellners.finscale.identity.config.ANONYMOUS_USER
import org.muellners.finscale.identity.config.audit.AuditEventConverter
import org.muellners.finscale.identity.domain.PersistentAuditEvent
import org.slf4j.LoggerFactory
import org.springframework.boot.actuate.audit.AuditEvent
import org.springframework.boot.actuate.audit.AuditEventRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

private const val AUTHORIZATION_FAILURE = "AUTHORIZATION_FAILURE"

/**
 * Should be the same as in Liquibase migration.
 */
const val EVENT_DATA_COLUMN_MAX_LENGTH: Int = 255

/**
 * An implementation of Spring Boot's {@link AuditEventRepository}.
 */
@Repository
class CustomAuditEventRepository(
    private val persistenceAuditEventRepository: PersistenceAuditEventRepository,
    private val auditEventConverter: AuditEventConverter
) : AuditEventRepository {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun find(principal: String, after: Instant, type: String): List<AuditEvent> {
        val persistentAuditEvents =
            persistenceAuditEventRepository.findByPrincipalAndAuditEventDateAfterAndAuditEventType(
                principal, after, type
            )
        return auditEventConverter.convertToAuditEvent(persistentAuditEvents)
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    override fun add(event: AuditEvent) {
        if (AUTHORIZATION_FAILURE != event.type && ANONYMOUS_USER != event.principal) {

            val persistentAuditEvent = PersistentAuditEvent(
                principal = event.principal,
                auditEventType = event.type,
                auditEventDate = event.timestamp
            )
            val eventData = auditEventConverter.convertDataToStrings(event.data)
            persistentAuditEvent.data = truncate(eventData)
            persistenceAuditEventRepository.save(persistentAuditEvent)
        }
    }

    /**
     * Truncate event data that might exceed column length.
     */
    private fun truncate(data: Map<String, String?>?): MutableMap<String, String?> {
        val results = mutableMapOf<String, String?>()

        if (data != null) {
            for (entry in data.entries) {
                var value: String? = entry.value
                if (value != null) {
                    val length = value.length
                    if (length > EVENT_DATA_COLUMN_MAX_LENGTH) {
                        value = value.substring(0, EVENT_DATA_COLUMN_MAX_LENGTH)
                        log.warn(
                            "Event data for ${entry.key} too long ($length) has been truncated to $EVENT_DATA_COLUMN_MAX_LENGTH. Consider increasing column width."
                        )
                    }
                }
                results[entry.key] = value
            }
        }
        return results
    }
}
