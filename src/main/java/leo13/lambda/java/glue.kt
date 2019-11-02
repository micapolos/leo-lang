package leo13.lambda.java

import leo13.lambda.*

typealias JavaExpr = Expr<Java>
typealias JavaAbstraction = Abstraction<JavaExpr>
typealias JavaApplication = Application<JavaExpr>
typealias JavaVariable = Variable<Java>

fun JavaVariable.index(gen: Gen) =
	gen.depth - index - 1
