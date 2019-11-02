package leo13.js.compiler

data class Arrow(val lhs: Type, val rhs: Type)

infix fun Type.arrowTo(rhs: Type) = Arrow(this, rhs)