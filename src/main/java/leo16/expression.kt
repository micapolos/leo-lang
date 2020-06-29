package leo16

import leo13.Stack

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
