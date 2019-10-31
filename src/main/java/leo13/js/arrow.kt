package leo13.js

data class Arrow(val lhs: Types, val rhs: Types)

infix fun Types.arrowTo(rhs: Types) = Arrow(this, rhs)