package leo16

import leo13.Stack

sealed class Expression
data class ValueExpression(val value: Value) : Expression()
data class StructExpression(val struct: Struct) : Expression()
data class ApplyExpression(val apply: Apply) : Expression()
data class VariableExpression(val variable: Variable) : Expression()

data class Apply(val lhs: Expression, val op: Op)

sealed class Op
data class GetOp(val get: Get) : Op()
data class SelectOp(val select: Select) : Op()
data class SwitchOp(val switch: Switch) : Op()
data class InvokeOp(val invoke: Invoke) : Op()

data class Struct(val expressionStack: Stack<Expression>)
data class Switch(val expressionStack: Stack<Expression>)
data class Get(val index: Int)
data class Select(val index: Int)
data class Variable(val index: Int)
data class Invoke(val expression: Expression)
