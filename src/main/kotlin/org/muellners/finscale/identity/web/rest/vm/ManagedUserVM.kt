package org.muellners.finscale.identity.web.rest.vm

import javax.validation.constraints.Size
import org.muellners.finscale.identity.service.dto.UserDTO

/**
 * View Model extending the [UserDTO], which is meant to be used in the user management UI.
 */
class ManagedUserVM : UserDTO() {

    @field:Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    var password: String? = null

    override fun toString() = "ManagedUserVM{${super.toString()}}"

    companion object {
        const val PASSWORD_MIN_LENGTH: Int = 4
        const val PASSWORD_MAX_LENGTH: Int = 100
    }
}
