package leo21.prim.scheme

import leo14.lambda.scheme.Code
import leo14.lambda.scheme.code
import leo14.literalString
import leo21.prim.DoubleMinusDoublePrim
import leo21.prim.DoublePlusDoublePrim
import leo21.prim.DoublePrim
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
			DoublePlusDoublePrim -> op2Code("+")
			DoubleMinusDoublePrim -> op2Code("-")
			DoubleTimesDoublePrim -> op2Code("*")
			StringPlusStringPrim -> op2Code("string-append")
		}

fun op2Code(name: String): Code = code("(lambda (a) (lambda (b) ($name a b)))")
fun op2Code(lhs: Code, name: String): Code = code("(lambda (a) ($name $lhs b))")
