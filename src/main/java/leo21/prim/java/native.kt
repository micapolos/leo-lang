package leo21.prim.java

import leo14.Number
import leo14.lambda.code.code
import leo14.lambda.java.Native
import leo14.lambda.java.native
import leo14.lambda.java.nullNative
import leo14.literalString
import leo21.prim.BooleanPrim
import leo21.prim.BooleanSwitchPrim
import leo21.prim.NilPrim
import leo21.prim.NumberCosinusPrim
import leo21.prim.NumberEqualsNumberPrim
import leo21.prim.NumberMinusNumberPrim
import leo21.prim.NumberPlusNumberPrim
import leo21.prim.NumberPrim
import leo21.prim.NumberSinusPrim
import leo21.prim.NumberStringPrim
import leo21.prim.NumberTimesNumberPrim
import leo21.prim.Prim
import leo21.prim.StringEqualsStringPrim
import leo21.prim.StringLengthPrim
import leo21.prim.StringPlusStringPrim
import leo21.prim.StringPrim
import leo21.prim.StringTryNumberPrim

val String.native: Native get() = native(code(literalString))
val Number.native: Native get() = native(code("${bigDecimal.toDouble()}"))

val Prim.native: Native
	get() =
		when (this) {
			is NilPrim -> nullNative
			is BooleanPrim -> TODO()
			is StringPrim -> string.native
			is NumberPrim -> number.native
			BooleanSwitchPrim -> TODO()
			NumberPlusNumberPrim -> op2Native("+", "Double")
			NumberMinusNumberPrim -> op2Native("-", "Double")
			NumberTimesNumberPrim -> op2Native("*", "Double")
			NumberEqualsNumberPrim -> fn2Native("equals", "Double")
			StringPlusStringPrim -> op2Native("+", "String")
			NumberSinusPrim -> prefixFn1Native("java.lang.Math.sin", "Double")
			NumberCosinusPrim -> prefixFn1Native("java.lang.Math.cos", "Double")
			StringLengthPrim -> postfixFn1Native("length()", "String")
			NumberStringPrim -> TODO()
			StringTryNumberPrim -> TODO()
			StringEqualsStringPrim -> TODO()
		}

val lhs = "fn(a->fn(b->a))"
val rhs = "fn(a->fn(b->b))"

fun op2Native(op: String, type: String) =
	native(code("fn(x->($type)apply(x, $lhs)${op}(${type})apply(x, $rhs))"))

fun fn2Native(fn: String, type: String) =
	native(code("fn(x->($type)apply(x, $lhs)${fn}((${type})apply(x, $rhs)))"))

fun prefixFn1Native(name: String, type: String) =
	native(code("fn(x->${name}((${type})x))"))

fun postfixFn1Native(name: String, type: String) =
	native(code("fn(x->((${type})x).${name}"))
