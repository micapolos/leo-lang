package leo21.prim.scheme

import leo14.lambda.scheme.code

fun fn2Code(op: String) =
	code("(lambda (x) ($op (x (lambda (a) (lambda (b) a))) (x (lambda (a) (lambda (b) b))))")
