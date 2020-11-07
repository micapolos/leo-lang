package leo21.prim.julia

import leo14.lambda.julia.Julia
import leo14.lambda.julia.julia
import leo14.literalString
import leo21.prim.DoubleCosinusPrim
import leo21.prim.DoubleMinusDoublePrim
import leo21.prim.DoublePlusDoublePrim
import leo21.prim.DoublePrim
import leo21.prim.DoubleSinusPrim
import leo21.prim.DoubleTimesDoublePrim
import leo21.prim.NilPrim
import leo21.prim.Prim
import leo21.prim.StringPlusStringPrim
import leo21.prim.StringPrim

val nullJulia = julia("null")
val String.julia: Julia get() = julia(literalString)
val Double.julia: Julia get() = julia("$this")

val Prim.julia: Julia
	get() =
		when (this) {
			is NilPrim -> nullJulia
			is StringPrim -> string.julia
			is DoublePrim -> double.julia
			DoublePlusDoublePrim -> op2Julia("+")
			DoubleMinusDoublePrim -> op2Julia("-")
			DoubleTimesDoublePrim -> op2Julia("*")
			StringPlusStringPrim -> op2Julia("+")
			DoubleSinusPrim -> op1Julia("sin")
			DoubleCosinusPrim -> op1Julia("cos")
		}

fun op1Julia(op: String) = julia("x->${op}(x)")
fun op2Julia(op: String) = julia("x->x(a->b->a)${op}x(a->b->b)")
