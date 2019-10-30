package leo13.js

data class TypeLine(val string: String, val type: Type)

infix fun String.lineTo(type: Type) = TypeLine(this, type)