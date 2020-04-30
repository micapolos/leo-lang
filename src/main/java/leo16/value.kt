package leo16

import leo13.Stack
import leo13.isEmpty
import leo13.push
import leo13.stack

sealed class Value
data class StructValue(val struct: Struct) : Value()
data class FunctionValue(val function: Function) : Value()
data class Struct(val lineStack: Stack<Line>)
data class Line(val word: String, val value: Value)

val Struct.value: Value get() = StructValue(this)
val Function.value: Value get() = FunctionValue(this)
val Stack<Line>.struct get() = Struct(this)
fun value(vararg lines: Line) = stack(*lines).struct.value
infix fun String.invoke(value: Value) = Line(this, value)

val Value.structOrNull: Struct? get() = (this as? StructValue)?.struct
val Value.functionOrNull: Function? get() = (this as? FunctionValue)?.function
val Struct.isEmpty get() = lineStack.isEmpty
val Value.isEmpty get() = structOrNull?.isEmpty ?: false

operator fun Struct.plus(line: Line): Struct = lineStack.push(line).struct
operator fun Value.plus(line: Line): Value? = structOrNull?.plus(line)?.value