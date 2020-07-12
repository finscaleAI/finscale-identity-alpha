package org.muellners.finscale.identity.domain

object TenantContextHolder {
    private val context: ThreadLocal<String> = ThreadLocal()

    fun get(): String {
        return try {
            context.get()
        } catch (e: IllegalStateException) {
            "main"
        }
    }

    fun set(identifier: String) = context.set(identifier)

    fun clear() = context.remove()
}
