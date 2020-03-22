package leo14.untyped

import leo.base.fold
import leo13.*
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

data class NativeValue(val native: Native) : Value() {
	override fun toString() = native.toString()
}

data class Field(val name: String, val rhs: Program) {
	override fun toString() = scriptField.toString()
}

sealed class Atom
data class LiteralAtom(val literal: Literal) : Atom()
data class FunctionAtom(val function: Function) : Atom()

val emptyProgram: Program get() = EmptyProgram
fun program(sequence: Sequence): Program = SequenceProgram(sequence)
fun program(vararg values: Value) = emptyProgram.fold(values) { plus(it) }
fun program(name: String) = program(value(name))
fun program(literal: Literal) = program(value(literal))
operator fun Program.plus(value: Value) = program(this sequenceTo value)

tailrec fun <R> R.foldValues(program: Program, fn: R.(Value) -> R): R =
	when (program) {
		EmptyProgram -> this
		is SequenceProgram -> fn(program.sequence.head).foldValues(program.sequence.tail, fn)
	}
val Program.valueStack: Stack<Value>
	get() =
		stack<Value>().foldValues(this) { push(it) }

operator fun Program.plus(program: Program) = fold(program.valueStack) { plus(it) }

fun value(literal: Literal): Value = LiteralValue(literal)
fun value(field: Field): Value = FieldValue(field)
fun value(name: String): Value = value(name fieldTo program())
fun value(function: Function): Value = FunctionValue(function)
fun value(native: Native): Value = NativeValue(native)

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
val Program.nativeOrNull get() = onlyValueOrNull?.nativeOrNull
val Program.functionOrNull get() = onlyValueOrNull?.functionOrNull
val Program.headOrNull get() = sequenceOrNull?.head?.let { program(it) }
val Program.tailOrNull get() = sequenceOrNull?.tail
val Program.lastOrNull get() = contentsOrNull?.headOrNull?.make("last")
val Program.previousOrNull get() = onlyFieldOrNull?.let { field -> field.rhs.tailOrNull?.let { tail -> program(field.name valueTo tail) } }
val Program.onlyNameOrNull get() = onlyValueOrNull?.onlyNameOrNull

val Sequence.onlyValueOrNull get() = if (tail.isEmpty) head else null

val Value.literalOrNull get() = (this as? LiteralValue)?.literal
val Value.fieldOrNull get() = (this as? FieldValue)?.field
val Value.functionOrNull get() = (this as? FunctionValue)?.function
val Value.nativeOrNull get() = (this as? NativeValue)?.native
val Value.onlyNameOrNull get() = fieldOrNull?.onlyNameOrNull

val Field.onlyNameOrNull get() = if (rhs.isEmpty) name else null
val Field.rhsHeadOrNull get() = rhs.headOrNull?.let { head -> program(name valueTo head) }
val Field.rhsTailOrNull get() = rhs.tailOrNull?.let { tail -> program(name valueTo tail) }

fun Program.make(name: String) = program(name valueTo this)
val Program._this get() = make(thisName)
