package leo21.prim.julia

import leo14.Number
import leo14.lambda.julia.Julia
import leo14.lambda.julia.julia
import leo14.literalString
import leo21.prim.NumberCosinusPrim
import leo21.prim.NumberMinusNumberPrim
import leo21.prim.NumberPlusNumberPrim
import leo21.prim.NumberPrim
import leo21.prim.NumberSinusPrim
import leo21.prim.NumberTimesNumberPrim
import leo21.prim.NilPrim
import leo21.prim.NumberEqualsNumberPrim
import leo21.prim.NumberStringPrim
import leo21.prim.Prim
import leo21.prim.StringLengthPrim
import leo21.prim.StringPlusStringPrim
import leo21.prim.StringPrim
import leo21.prim.StringTryNumberPrim

val nullJulia = julia("null")
val String.julia: Julia get() = julia(literalString)
val Number.julia: Julia get() = julia("$this")

val Prim.julia: Julia
	get() =
		when (this) {
			is NilPrim -> nullJulia
			is StringPrim -> string.julia
			is NumberPrim -> number.julia
			NumberPlusNumberPrim -> op2Julia("+")
			NumberMinusNumberPrim -> op2Julia("-")
			NumberTimesNumberPrim -> op2Julia("*")
			StringPlusStringPrim -> op2Julia("+")
			NumberSinusPrim -> op1Julia("sin")
			NumberCosinusPrim -> op1Julia("cos")
			NumberEqualsNumberPrim -> TODO()
			StringLengthPrim -> TODO()
			NumberStringPrim -> TODO()
			StringTryNumberPrim -> TODO()
		}

fun op1Julia(op: String) = julia("x->${op}(x)")
fun op2Julia(op: String) = julia("x->x(a->b->a)${op}x(a->b->b)")
