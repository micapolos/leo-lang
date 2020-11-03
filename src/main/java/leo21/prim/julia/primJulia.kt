package leo21.prim.julia

import leo14.lambda.julia.Julia
import leo14.lambda.julia.julia
import leo14.literalString
import leo21.prim.DoubleMinusDoublePrim
import leo21.prim.DoublePlusDoublePrim
import leo21.prim.DoublePrim
import leo21.prim.DoubleTimesDoublePrim
import leo21.prim.MinusDoublePrim
import leo21.prim.PlusDoublePrim
import leo21.prim.PlusStringPrim
import leo21.prim.Prim
import leo21.prim.StringPlusStringPrim
import leo21.prim.StringPrim
import leo21.prim.TimesDoublePrim

val String.julia: Julia get() = julia(literalString)
val Double.julia: Julia get() = julia("$this")

val Prim.julia: Julia
	get() =
		when (this) {
			is StringPrim -> string.julia
			is DoublePrim -> double.julia
			DoublePlusDoublePrim -> op2Julia("+")
			DoubleMinusDoublePrim -> op2Julia("-")
			DoubleTimesDoublePrim -> op2Julia("*")
			StringPlusStringPrim -> op2Julia("+")
			is PlusDoublePrim -> op2Julia(double.julia, "+")
			is MinusDoublePrim -> op2Julia(double.julia, "-")
			is TimesDoublePrim -> op2Julia(double.julia, "*")
			is PlusStringPrim -> op2Julia(string.julia, "string-append")
		}

fun op2Julia(op: String) = julia("a->b->a${op}b")
fun op2Julia(lhs: Julia, op: String) = julia("a->($lhs)${op}a")
