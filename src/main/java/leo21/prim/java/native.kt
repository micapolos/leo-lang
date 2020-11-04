package leo21.prim.java

import leo14.lambda.code.code
import leo14.lambda.java.Native
import leo14.lambda.java.native
import leo14.lambda.java.nullNative
import leo14.lambda.julia.julia
import leo14.literalString
import leo21.prim.DoubleMinusDoublePrim
import leo21.prim.DoublePlusDoublePrim
import leo21.prim.DoublePrim
import leo21.prim.DoubleTimesDoublePrim
import leo21.prim.NilPrim
import leo21.prim.Prim
import leo21.prim.StringPlusStringPrim
import leo21.prim.StringPrim

val String.native: Native get() = native(code(literalString))
val Double.native: Native get() = native(code("$this"))

val Prim.native: Native
	get() =
		when (this) {
			is NilPrim -> nullNative
			is StringPrim -> string.native
			is DoublePrim -> double.native
			DoublePlusDoublePrim -> op2Native("+", "Double")
			DoubleMinusDoublePrim -> op2Native("-", "Double")
			DoubleTimesDoublePrim -> op2Native("*", "Double")
			StringPlusStringPrim -> op2Native("+", "String")
		}

val lhs = "fn(a->fn(b->a))"
val rhs = "fn(a->fn(b->b))"

fun op2Native(op: String, type: String) =
	native(code("fn(x->($type)apply(x, $lhs)${op}(${type})apply(x, $rhs))"))
