package tech.ccat.znitem.validation

data class ValidationResult(
    val valid: Boolean,
    val errors: List<String> = emptyList()
) {
    companion object {
        fun ok(): ValidationResult = ValidationResult(true)

        fun error(message: String): ValidationResult = ValidationResult(false, listOf(message))

        fun errors(messages: List<String>): ValidationResult = ValidationResult(false, messages)
    }
}
