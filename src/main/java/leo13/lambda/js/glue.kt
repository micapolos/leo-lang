package leo13.lambda.js

import leo13.Index
import leo13.index0
import leo13.index1
import leo13.index2
import leo13.js.ast.Expr
import leo13.js.ast.expr
import leo13.js.ast.id
import leo13.lambda.code.Code
import leo13.lambda.value

typealias Value = leo13.lambda.Value<Expr>

fun arg(index: Index): Value = leo13.lambda.arg(index)
val arg0 get() = arg(index0)
val arg1 get() = arg(index1)
val arg2 get() = arg(index2)

fun value(code: Code) = value(expr(id(code.string)))
fun value(int: Int) = value(expr(int))
fun value(double: Double) = value(expr(double))
fun value(string: String) = value(expr(string))
