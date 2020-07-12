package org.muellners.finscale.identity.domain

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource

class ContextAwareRoutingDataSource : AbstractRoutingDataSource() {

    override fun determineCurrentLookupKey(): Any? {
        afterPropertiesSet() // ???
        return TenantContextHolder.get()
    }
}
