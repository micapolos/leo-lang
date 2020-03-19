package leo14.untyped

import leo.base.fold
import leo13.fold
import leo13.push
import leo13.stack
import leo14.Literal
import leo14.numberOrNull
import leo14.stringOrNull

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

sealed class Atom
data class LiteralAtom(val literal: Literal) : Atom()
data class FunctionAtom(val function: Function) : Atom()

fun program(): Program = EmptyProgram
fun program(sequence: Sequence): Program = SequenceProgram(sequence)
fun program(value: Value, vararg values: Value) = program(program() sequenceTo value).fold(values) { plus(it) }
fun program(name: String) = program(value(name))
fun program(literal: Literal) = program(value(literal))
operator fun Program.plus(value: Value) = program(this sequenceTo value)

tailrec fun <R> R.foldValues(program: Program, fn: R.(Value) -> R): R =
	when (program) {
		EmptyProgram -> this
		is SequenceProgram -> fn(program.sequence.head).foldValues(program.sequence.tail, fn)
	}

operator fun Program.plus(program: Program) = fold(stack<Value>().foldValues(program) { push(it) }) { plus(it) }

fun value(literal: Literal): Value = LiteralValue(literal)
fun value(field: Field): Value = FieldValue(field)
fun value(name: String): Value = value(name fieldTo program())
fun value(function: Function): Value = FunctionValue(function)

fun Sequence.plus(value: Value) = program(this) sequenceTo value
fun sequence(value: Value, vararg values: Value) = program().sequenceTo(value).fold(values, Sequence::plus)
fun sequence(name: String) = sequence(value(name))
infix fun Program.sequenceTo(head: Value) = Sequence(this, head)
infix fun String.fieldTo(program: Program) = Field(this, program)
infix fun String.valueTo(program: Program) = value(this fieldTo program)

val Program.isEmpty get() = this is EmptyProgram
val Program.sequenceOrNull get() = (this as? SequenceProgram)?.sequence
val Program.onlyValueOrNull get() = sequenceOrNull?.onlyValueOrNull
val Program.onlyFieldOrNull get() = onlyValueOrNull?.fieldOrNull
val Program.contentsOrNull get() = onlyFieldOrNull?.rhs
val Program.numberOrNull get() = onlyValueOrNull?.literalOrNull?.numberOrNull
val Program.textOrNull get() = onlyValueOrNull?.literalOrNull?.stringOrNull
val Program.functionOrNull get() = onlyValueOrNull?.functionOrNull
val Program.headOrNull get() = sequenceOrNull?.head?.let { program(it) }
val Program.tailOrNull get() = sequenceOrNull?.tail
val Program.onlyNameOrNull get() = onlyValueOrNull?.onlyNameOrNull

val Sequence.onlyValueOrNull get() = if (tail.isEmpty) head else null

val Value.literalOrNull get() = (this as? LiteralValue)?.literal
val Value.fieldOrNull get() = (this as? FieldValue)?.field
val Value.functionOrNull get() = (this as? FunctionValue)?.function
val Value.onlyNameOrNull get() = fieldOrNull?.onlyNameOrNull

val Field.onlyNameOrNull get() = if (rhs.isEmpty) name else null

fun Program.make(name: String) = program(name valueTo this)
