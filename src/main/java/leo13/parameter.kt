package leo13

import leo13.type.Type

data class TypeParameter(val type: Type)

fun parameter(type: Type) = TypeParameter(type)
