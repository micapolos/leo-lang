package leo21.prim.scheme

import leo14.Number
import leo14.lambda.scheme.Code
import leo14.lambda.scheme.code
import leo14.literalString
import leo21.prim.NumberCosinusPrim
import leo21.prim.NumberMinusNumberPrim
import leo21.prim.NumberPlusNumberPrim
import leo21.prim.NumberPrim
import leo21.prim.NumberSinusPrim
import leo21.prim.NumberTimesNumberPrim
import leo21.prim.NilPrim
import leo21.prim.Prim
import leo21.prim.StringPlusStringPrim
import leo21.prim.StringPrim

val nilCode = code("'()")
val String.code: Code get() = code(literalString)
val Number.code: Code get() = code("$this")

val Prim.code: Code
	get() =
		when (this) {
			is NilPrim -> nilCode
			is StringPrim -> string.code
			is NumberPrim -> number.code
			NumberPlusNumberPrim -> fn2Code("+")
			NumberMinusNumberPrim -> fn2Code("-")
			NumberTimesNumberPrim -> fn2Code("*")
			StringPlusStringPrim -> fn2Code("string-append")
			NumberSinusPrim -> fn1Code("sin")
			NumberCosinusPrim -> fn1Code("cos")
		}

val firstCode = code("(lambda (a) (lambda (b) a))")
val secondCode = code("(lambda (a) (lambda (b) b))")

fun fn1Code(op: String) =
	code("(lambda (x) ($op x))")

fun fn2Code(op: String) =
	code("(lambda (x) ($op (x $firstCode) (x $secondCode)))")

