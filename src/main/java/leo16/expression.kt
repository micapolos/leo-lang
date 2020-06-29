package leo16

import leo13.Stack
import leo13.stack

sealed class Expression
data class ValueExpression(val value: Value) : Expression()
data class MakeExpression(val make: Make) : Expression()
data class ApplyExpression(val apply: Apply) : Expression()
data class VariableExpression(val variable: Variable) : Expression()

data class Make(val lineStack: Stack<Line>)
data class Line(val word: String, val rhs: Expression)

data class Apply(val lhs: Expression, val op: Op)

sealed class Op
data class GetOp(val get: Get) : Op()
data class SwitchOp(val switch: Switch) : Op()
data class InvokeOp(val invoke: Invoke) : Op()

data class Switch(val lineStack: Stack<Line>)
data class Get(val word: String)
data class Variable(val index: Int)
data class Invoke(val expression: Expression)

val Value.expression: Expression get() = ValueExpression(this)
fun expression(vararg lines: Line): Expression = MakeExpression(make(*lines))
fun Expression.make(word: String): Expression = expression(word(this))
fun Expression.get(word: String): Expression = ApplyExpression(Apply(this, GetOp(Get(word))))
fun make(vararg lines: Line) = Make(stack(*lines))
operator fun String.invoke(expression: Expression) = Line(this, expression)
