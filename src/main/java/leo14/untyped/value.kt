package leo14.untyped

import leo.base.fold
import leo13.*
import leo14.Literal
import leo14.numberOrNull
import leo14.stringOrNull

sealed class Value

object EmptyValue : Value() {
	override fun toString() = script.toString()
}

data class SequenceValue(val sequence: Sequence) : Value() {
	override fun toString() = script.toString()
}

data class Sequence(val tail: Thunk, val head: Line) {
	override fun toString() = script.toString()
}

sealed class Line

data class LiteralLine(val literal: Literal) : Line() {
	override fun toString() = scriptLine.toString()
}

data class FieldLine(val field: Field) : Line() {
	override fun toString() = scriptLine.toString()
}

data class FunctionLine(val function: Function) : Line() {
	override fun toString() = scriptLine.toString()
}

data class NativeLine(val native: Native) : Line() {
	override fun toString() = native.toString()
}

data class Field(val name: String, val thunk: Thunk) {
	override fun toString() = scriptField.toString()
}

sealed class Atom
data class LiteralAtom(val literal: Literal) : Atom()
data class FunctionAtom(val function: Function) : Atom()

val emptyValue: Value get() = EmptyValue
fun value(sequence: Sequence): Value = SequenceValue(sequence)
fun value(vararg lines: Line) = emptyValue.fold(lines) { plus(it) }
fun value(name: String) = value(line(name))
fun value(literal: Literal) = value(line(literal))
operator fun Value.plus(line: Line) = value(this sequenceTo line)

tailrec fun <R> R.foldValues(value: Value, fn: R.(Line) -> R): R =
	when (value) {
		EmptyValue -> this
		is SequenceValue -> fn(value.sequence.head).foldValues(value.sequence.tail.value, fn)
	}

val Value.lineStack: Stack<Line>
	get() =
		stack<Line>().foldValues(this) { push(it) }

operator fun Value.plus(value: Value) = fold(value.lineStack) { plus(it) }

fun line(literal: Literal): Line = LiteralLine(literal)
fun line(field: Field): Line = FieldLine(field)
fun line(name: String): Line = line(name fieldTo value())
fun line(function: Function): Line = FunctionLine(function)
fun line(native: Native): Line = NativeLine(native)

fun Sequence.plus(line: Line) = value(this) sequenceTo line
fun sequence(line: Line, vararg lines: Line) = value().sequenceTo(line).fold(lines, Sequence::plus)
fun sequence(name: String) = sequence(line(name))
infix fun Thunk.sequenceTo(head: Line) = Sequence(this, head)
infix fun Value.sequenceTo(head: Line) = thunk(this) sequenceTo head
infix fun String.fieldTo(thunk: Thunk) = Field(this, thunk)
infix fun String.fieldTo(value: Value) = this fieldTo thunk(value)
infix fun String.lineTo(thunk: Thunk) = line(this fieldTo thunk)
infix fun String.lineTo(value: Value) = this lineTo thunk(value)

val Value.isEmpty get() = this is EmptyValue
val Value.sequenceOrNull get() = (this as? SequenceValue)?.sequence
val Value.onlyLineOrNull get() = sequenceOrNull?.onlyValueOrNull
val Value.onlyFieldOrNull get() = onlyLineOrNull?.fieldOrNull
val Value.contentsOrNull get() = onlyFieldOrNull?.rhs
val Value.nameOrNull get() = onlyFieldOrNull?.name
val Value.numberOrNull get() = onlyLineOrNull?.literalOrNull?.numberOrNull
val Value.textOrNull get() = onlyLineOrNull?.literalOrNull?.stringOrNull
val Value.nativeOrNull get() = onlyLineOrNull?.nativeOrNull
val Value.functionOrNull get() = onlyLineOrNull?.functionOrNull
val Value.headOrNull get() = sequenceOrNull?.head?.let { value(it) }
val Value.tailOrNull get() = sequenceOrNull?.tail?.value
val Value.lastOrNull get() = contentsOrNull?.headOrNull?.make(lastName)
val Value.previousOrNull
	get() =
		onlyFieldOrNull?.let { field ->
			field.rhs.tailOrNull?.let { tail ->
				value(
					previousName lineTo value(
						field.name lineTo tail))
			}
		}
val Value.onlyNameOrNull get() = onlyLineOrNull?.onlyNameOrNull

val Sequence.onlyValueOrNull get() = if (tail.value.isEmpty) head else null

val Line.literalOrNull get() = (this as? LiteralLine)?.literal
val Line.fieldOrNull get() = (this as? FieldLine)?.field
val Line.functionOrNull get() = (this as? FunctionLine)?.function
val Line.nativeOrNull get() = (this as? NativeLine)?.native
val Line.onlyNameOrNull get() = fieldOrNull?.onlyNameOrNull

val Field.rhs get() = thunk.value
val Field.onlyNameOrNull get() = if (rhs.isEmpty) name else null
val Field.rhsHeadOrNull get() = rhs.headOrNull?.let { head -> value(name lineTo head) }
val Field.rhsTailOrNull get() = rhs.tailOrNull?.let { tail -> value(name lineTo tail) }

fun Value.make(name: String) = value(name lineTo this)
fun Thunk.make(name: String) = thunk(value(name lineTo this))
val Thunk.this_ get() = make(thisName)
