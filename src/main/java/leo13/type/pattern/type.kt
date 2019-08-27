package leo13.type.pattern

data class Type(val pattern: Pattern)

fun type(pattern: Pattern) = Type(pattern)

fun Type.contains(type: Type) = this == type