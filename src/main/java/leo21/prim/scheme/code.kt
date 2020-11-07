package leo21.prim.scheme

import leo14.lambda.scheme.Code
import leo14.lambda.scheme.code
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

val nilCode = code("'()")
val String.code: Code get() = code(literalString)
val Double.code: Code get() = code("$this")

val Prim.code: Code
	get() =
		when (this) {
			is NilPrim -> nilCode
			is StringPrim -> string.code
			is DoublePrim -> double.code
			DoublePlusDoublePrim -> fn2Code("+")
			DoubleMinusDoublePrim -> fn2Code("-")
			DoubleTimesDoublePrim -> fn2Code("*")
			StringPlusStringPrim -> fn2Code("string-append")
			DoubleSinusPrim -> fn1Code("sin")
			DoubleCosinusPrim -> fn1Code("cos")
		}

val firstCode = code("(lambda (a) (lambda (b) a))")
val secondCode = code("(lambda (a) (lambda (b) b))")

fun fn1Code(op: String) =
	code("(lambda (x) ($op x))")

fun fn2Code(op: String) =
	code("(lambda (x) ($op (x $firstCode) (x $secondCode)))")

