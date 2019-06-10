package leo5.type

data class TypeParameter(val type: Type)

fun parameter(type: Type) = TypeParameter(type)
