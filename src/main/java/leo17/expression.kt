package leo17

import leo13.Stack

data class Typed<out T, out V>(val type: Type<T>, val expression: Expression<V>)

sealed class Expression<out V>
data class ValueExpression<out V>(val value: V) : Expression<V>()
data class StructExpression<out V>(val struct: Struct<V>) : Expression<V>()
data class ChoiceExpression<out V>(val choice: Choice<V>) : Expression<V>()
data class ApplicationExpression<out V>(val application: Application<V>) : Expression<V>()

data class Struct<out V>(val expressionStack: Stack<Expression<V>>)
data class Choice<out V>(val index: Int, val expression: Expression<V>)
data class Application<out V>(val lhs: Expression<V>, val op: Op<V>)

sealed class Op<out V>
data class GetOp<out V>(val get: Get<V>) : Op<V>()
data class SwitchOp<out V>(val switch: Switch<V>) : Op<V>()

data class Get<out V>(val index: Int)
data class Select<out V>(val index: Int)
data class Switch<out V>(val expressionStack: Stack<Expression<V>>)
