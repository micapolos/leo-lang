package leo16

import leo13.*
import leo15.structName
import leo15.valueName

sealed class Value {
	override fun toString() = asSentence.toString()
}

data class StructValue(val struct: Struct) : Value() {
	override fun toString() = super.toString()
}

data class FunctionValue(val function: Function) : Value() {
	override fun toString() = super.toString()
}

data class Struct(val lineStack: Stack<Line>) {
	override fun toString() = asSentence.toString()
}

data class Line(val word: String, val value: Value) {
	override fun toString() = asSentence.toString()
}

val Value.asSentence: Sentence
	get() =
		valueName(asScript)

val Value.asScript: Script
	get() =
		when (this) {
			is StructValue -> struct.asScript
			is FunctionValue -> function.asSentence.script
		}

val Struct.asSentence: Sentence
	get() =
		structName(asScript)

val Struct.asScript: Script
	get() =
		lineStack.map { asSentence }.script

val Line.asSentence: Sentence
	get() =
		word.invoke(value.asScript)

val Struct.value: Value get() = StructValue(this)
val Function.value: Value get() = FunctionValue(this)
val Stack<Line>.struct get() = Struct(this)
fun value(vararg lines: Line) = stack(*lines).struct.value
operator fun String.invoke(value: Value) = Line(this, value)
operator fun String.invoke(line: Line, vararg lines: Line) = invoke(stack(line, *lines).struct.value)

val Value.structOrNull: Struct? get() = (this as? StructValue)?.struct
val Value.functionOrNull: Function? get() = (this as? FunctionValue)?.function
val Struct.isEmpty get() = lineStack.isEmpty
val Value.isEmpty get() = structOrNull?.isEmpty ?: false

val Value.struct: Struct
	get() =
		when (this) {
			is StructValue -> struct
			is FunctionValue -> function.struct
		}

operator fun Struct.plus(line: Line): Struct = lineStack.push(line).struct
operator fun Value.plus(line: Line): Value = struct.plus(line).value