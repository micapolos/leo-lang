package leo14.untyped

import leo.base.fold
import leo14.Literal

sealed class Program

object EmptyProgram : Program() {
	override fun toString() = script.toString()
}

data class SequenceProgram(val sequence: Sequence) : Program() {
	override fun toString() = script.toString()
}

data class Sequence(val tail: Program, val head: Value) {
	override fun toString() = script.toString()
}

sealed class Value

data class LiteralValue(val literal: Literal) : Value() {
	override fun toString() = scriptLine.toString()
}

data class FieldValue(val field: Field) : Value() {
	override fun toString() = scriptLine.toString()
}

data class FunctionValue(val function: Function) : Value() {
	override fun toString() = scriptLine.toString()
}

data class Field(val name: String, val rhs: Program) {
	override fun toString() = scriptField.toString()
}

fun program(): Program = EmptyProgram
fun program(sequence: Sequence): Program = SequenceProgram(sequence)
fun program(value: Value, vararg values: Value) = program(program() sequenceTo value).fold(values, Program::plus)
fun program(name: String) = program(value(name))
fun program(literal: Literal) = program(value(literal))
operator fun Program.plus(value: Value) = program(this sequenceTo value)

fun value(literal: Literal): Value = LiteralValue(literal)
fun value(field: Field): Value = FieldValue(field)
fun value(name: String): Value = value(name fieldTo program())
fun value(function: Function): Value = FunctionValue(function)

infix fun Program.sequenceTo(head: Value) = Sequence(this, head)
infix fun String.fieldTo(program: Program) = Field(this, program)
infix fun String.valueTo(program: Program) = value(this fieldTo program)

