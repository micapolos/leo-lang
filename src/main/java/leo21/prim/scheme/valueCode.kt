package leo21.prim.scheme

import leo14.lambda.scheme.code

val firstCode = code("(lambda (a) (lambda (b) a))")
val secondCode = code("(lambda (a) (lambda (b) b))")

fun fn2Code(op: String) =
	code("(lambda (x) ($op (x $firstCode) (x $secondCode)))")
