package org.muellners.finscale.identity.service

class InvalidPasswordException : RuntimeException("Incorrect password") {
    companion object {
        private const val serialVersionUID = 1L
    }
}
