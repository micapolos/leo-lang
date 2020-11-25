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
import leo21.prim.NumberEqualsNumberPrim
import leo21.prim.NumberStringPrim
import leo21.prim.Prim
import leo21.prim.StringEqualsStringPrim
import leo21.prim.StringLengthPrim
import leo21.prim.StringPlusStringPrim
import leo21.prim.StringPrim
import leo21.prim.StringTryNumberPrim

val nilCode = code("'()")
val String.code: Code get() = code(literalString)
val Number.code: Code get() = code("$this")

val Prim.code: Code
	get() =
		when (this) {
			is NilPrim -> nilCode
			is StringPrim -> string.code
			is NumberPrim -> number.code
			NumberEqualsNumberPrim -> boolSwitchCode(fn2Code("="))
			NumberPlusNumberPrim -> fn2Code("+")
			NumberMinusNumberPrim -> fn2Code("-")
			NumberTimesNumberPrim -> fn2Code("*")
			NumberStringPrim -> fn1Code("number->string")
			NumberSinusPrim -> fn1Code("sin")
			NumberCosinusPrim -> fn1Code("cos")
			StringEqualsStringPrim -> boolSwitchCode(fn2Code("string=?"))
			StringPlusStringPrim -> fn2Code("string-append")
			StringLengthPrim -> fn1Code("string-length")
			StringTryNumberPrim -> fn1Code("string->number") // TODO: Convert to try
		}

val firstCode = code("(lambda (a) (lambda (b) a))")
val secondCode = code("(lambda (a) (lambda (b) b))")

fun fn1Code(op: String) =
	code("(lambda (x) ($op x))")

fun fn2Code(op: String) =
	code("(lambda (x) ($op (x $firstCode) (x $secondCode)))")

fun boolSwitchCode(boolCode: Code) =
	code("(lambda (b) (lambda (f1) (lambda (f2) (if ($boolCode b) (f1 b) (f2 b)))))")
