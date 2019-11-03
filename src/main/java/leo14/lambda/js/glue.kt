package leo14.lambda.js

import leo13.Index
import leo13.index0
import leo13.index1
import leo13.index2
import leo13.js.ast.Expr
import leo13.js.ast.expr
import leo13.js.ast.id
import leo14.lambda.code.Code
import leo14.lambda.code.code
import leo14.lambda.value

typealias Value = leo14.lambda.Value<Expr>

fun arg(index: Index): Value = leo14.lambda.arg(index)
val arg0 get() = arg(index0)
val arg1 get() = arg(index1)
val arg2 get() = arg(index2)

fun value(code: Code) = value(expr(id(code.string)))

val nullValue get() = value(code("null"))
fun value(boolean: Boolean) = value(code("$boolean"))
fun value(int: Int) = value(expr(int))
fun value(double: Double) = value(expr(double))
fun value(string: String) = value(expr(string))
