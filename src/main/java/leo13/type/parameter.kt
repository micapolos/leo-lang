package leo13.type

data class TypeParameter(val pattern: Pattern)

fun parameter(pattern: Pattern) = TypeParameter(pattern)
