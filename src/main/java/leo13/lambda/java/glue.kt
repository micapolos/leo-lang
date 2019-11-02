package leo13.lambda.java

import leo13.lambda.*

typealias JavaExpr = Expr<Java>
typealias JavaFn = Fn<JavaExpr>
typealias JavaAp = Ap<JavaExpr>
typealias JavaArg = Arg<Java>

fun JavaArg.index(gen: Gen) =
	gen.depth - index - 1
