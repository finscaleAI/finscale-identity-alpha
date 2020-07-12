package org.muellners.finscale.identity.repository

import java.time.Instant
import org.muellners.finscale.identity.domain.PersistentAuditEvent
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Spring Data JPA for the [PersistentAuditEvent] entity.
 */
interface PersistenceAuditEventRepository : JpaRepository<PersistentAuditEvent, Long> {

    fun findByPrincipal(principal: String): List<PersistentAuditEvent>

    fun findByPrincipalAndAuditEventDateAfterAndAuditEventType(
        principal: String,
        after: Instant,
        type: String
    ): List<PersistentAuditEvent>

    fun findAllByAuditEventDateBetween(
        fromDate: Instant,
        toDate: Instant,
        pageable: Pageable
    ): Page<PersistentAuditEvent>

    fun findByAuditEventDateBefore(before: Instant): List<PersistentAuditEvent>
}
