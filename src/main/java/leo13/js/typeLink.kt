package leo13.js

data class TypeLink(val type: Type, val line: TypeLine)

infix fun Type.linkTo(line: TypeLine) = TypeLink(this, line)