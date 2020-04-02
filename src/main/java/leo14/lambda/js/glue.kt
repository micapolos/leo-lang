package leo14.lambda.js

import leo13.Index
import leo14.js.ast.Expr
import leo14.js.ast.expr
import leo14.js.ast.id
import leo14.lambda.code.Code
import leo14.lambda.code.code
import leo14.lambda.term

typealias Term = leo14.lambda.Term<Expr>

fun arg(index: Index): Term = leo14.lambda.arg(index)
val arg0 get() = arg(0)
val arg1 get() = arg(1)
val arg2 get() = arg(2)

fun term(code: Code) = term(expr(id(code.string)))

val nullTerm get() = term(code("null"))
fun term(boolean: Boolean) = term(code("$boolean"))
fun term(int: Int) = term(expr(int))
fun term(double: Double) = term(expr(double))
fun term(string: String) = term(expr(string))
