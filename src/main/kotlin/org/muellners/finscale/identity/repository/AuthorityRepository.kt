package org.muellners.finscale.identity.repository

import org.muellners.finscale.identity.domain.Authority
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Spring Data JPA repository for the [Authority] entity.
 */

interface AuthorityRepository : JpaRepository<Authority, String>
