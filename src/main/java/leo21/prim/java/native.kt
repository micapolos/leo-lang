package leo21.prim.java

import leo14.Number
import leo14.lambda.code.code
import leo14.lambda.java.Native
import leo14.lambda.java.native
import leo14.lambda.java.nullNative
import leo14.literalString
import leo21.prim.NilPrim
import leo21.prim.NumberCosinusPrim
import leo21.prim.NumberMinusNumberPrim
import leo21.prim.NumberPlusNumberPrim
import leo21.prim.NumberPrim
import leo21.prim.NumberSinusPrim
import leo21.prim.NumberTimesNumberPrim
import leo21.prim.Prim
import leo21.prim.StringPlusStringPrim
import leo21.prim.StringPrim

val String.native: Native get() = native(code(literalString))
val Number.native: Native get() = native(code("${bigDecimal.toDouble()}"))

val Prim.native: Native
	get() =
		when (this) {
			is NilPrim -> nullNative
			is StringPrim -> string.native
			is NumberPrim -> number.native
			NumberPlusNumberPrim -> op2Native("+", "Double")
			NumberMinusNumberPrim -> op2Native("-", "Double")
			NumberTimesNumberPrim -> op2Native("*", "Double")
			StringPlusStringPrim -> op2Native("+", "String")
			NumberSinusPrim -> op1Native("java.lang.Math.sin", "Double")
			NumberCosinusPrim -> op1Native("java.lang.Math.cos", "Double")
		}

val lhs = "fn(a->fn(b->a))"
val rhs = "fn(a->fn(b->b))"

fun op2Native(op: String, type: String) =
	native(code("fn(x->($type)apply(x, $lhs)${op}(${type})apply(x, $rhs))"))

fun op1Native(name: String, type: String) =
	native(code("fn(x->${name}((${type})x))"))
