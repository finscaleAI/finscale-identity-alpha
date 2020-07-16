package org.muellners.finscale.identity.domain

import java.io.Serializable
import javax.validation.constraints.NotNull
import org.springframework.data.annotation.TypeAlias

/**
 * A DTO for the [org.muellners.finscale.core.domain.Tenant] entity.
 */
 @TypeAlias("TenantDTO")
// @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
data class TenantDTO(

    var id: Long? = null,

    @get: NotNull
    var identifier: String = "",

    @get: NotNull
    var name: String? = null,

    var description: String? = null

) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TenantDTO) return false
        return id != null && id == other.id
    }

    override fun hashCode() = 31
}
