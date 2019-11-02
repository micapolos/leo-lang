package leo13.lambda.java

import leo13.lambda.*

typealias JavaExpr = Expr<Java>
typealias JavaArrow = Arrow<JavaExpr>
typealias JavaLhs = Lhs<JavaExpr>
typealias JavaRhs = Rhs<JavaExpr>
typealias JavaFn = Fn<JavaExpr>
typealias JavaAp = Ap<JavaExpr>
typealias JavaArg = Arg<Java>

val jsArg = arg<Java>()

fun JavaArg.index(gen: Gen) =
	gen.depth - index - 1

fun expr(int: Int) = expr(Java("$int"))
fun expr(double: Double) = expr("$double")
fun expr(string: String) = expr(Java("\"$string\""))
