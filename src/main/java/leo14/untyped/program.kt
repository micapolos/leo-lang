@file:JvmName("ThunkKt")

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

val emptyProgram: Program get() = EmptyProgram
fun program(sequence: Sequence): Program = SequenceProgram(sequence)
fun program(vararg lines: Line) = emptyProgram.fold(lines) { plus(it) }
fun program(name: String) = program(line(name))
fun program(literal: Literal) = program(line(literal))
operator fun Program.plus(line: Line) = program(this sequenceTo line)

tailrec fun <R> R.foldValues(program: Program, fn: R.(Line) -> R): R =
	when (program) {
		EmptyProgram -> this
		is SequenceProgram -> fn(program.sequence.head).foldValues(program.sequence.tail.program, fn)
	}
val Program.lineStack: Stack<Line>
	get() =
		stack<Line>().foldValues(this) { push(it) }

operator fun Program.plus(program: Program) = fold(program.lineStack) { plus(it) }

fun line(literal: Literal): Line = LiteralLine(literal)
fun line(field: Field): Line = FieldLine(field)
fun line(name: String): Line = line(name fieldTo program())
fun line(function: Function): Line = FunctionLine(function)
fun line(native: Native): Line = NativeLine(native)

fun Sequence.plus(line: Line) = program(this) sequenceTo line
fun sequence(line: Line, vararg lines: Line) = program().sequenceTo(line).fold(lines, Sequence::plus)
fun sequence(name: String) = sequence(line(name))
infix fun Thunk.sequenceTo(head: Line) = Sequence(this, head)
infix fun Program.sequenceTo(head: Line) = thunk(this) sequenceTo head
infix fun String.fieldTo(thunk: Thunk) = Field(this, thunk)
infix fun String.fieldTo(program: Program) = this fieldTo thunk(program)
infix fun String.lineTo(thunk: Thunk) = line(this fieldTo thunk)
infix fun String.lineTo(program: Program) = this lineTo thunk(program)

val Program.isEmpty get() = this is EmptyProgram
val Program.sequenceOrNull get() = (this as? SequenceProgram)?.sequence
val Program.onlyLineOrNull get() = sequenceOrNull?.onlyValueOrNull
val Program.onlyFieldOrNull get() = onlyLineOrNull?.fieldOrNull
val Program.contentsOrNull get() = onlyFieldOrNull?.rhs
val Program.nameOrNull get() = onlyFieldOrNull?.name
val Program.numberOrNull get() = onlyLineOrNull?.literalOrNull?.numberOrNull
val Program.textOrNull get() = onlyLineOrNull?.literalOrNull?.stringOrNull
val Program.nativeOrNull get() = onlyLineOrNull?.nativeOrNull
val Program.functionOrNull get() = onlyLineOrNull?.functionOrNull
val Program.headOrNull get() = sequenceOrNull?.head?.let { program(it) }
val Program.tailOrNull get() = sequenceOrNull?.tail?.program
val Program.lastOrNull get() = contentsOrNull?.headOrNull?.make(lastName)
val Program.previousOrNull
	get() =
		onlyFieldOrNull?.let { field ->
			field.rhs.tailOrNull?.let { tail ->
				program(
					previousName lineTo program(
						field.name lineTo tail))
			}
		}
val Program.onlyNameOrNull get() = onlyLineOrNull?.onlyNameOrNull

val Sequence.onlyValueOrNull get() = if (tail.program.isEmpty) head else null

val Line.literalOrNull get() = (this as? LiteralLine)?.literal
val Line.fieldOrNull get() = (this as? FieldLine)?.field
val Line.functionOrNull get() = (this as? FunctionLine)?.function
val Line.nativeOrNull get() = (this as? NativeLine)?.native
val Line.onlyNameOrNull get() = fieldOrNull?.onlyNameOrNull

val Field.rhs get() = thunk.program
val Field.onlyNameOrNull get() = if (rhs.isEmpty) name else null
val Field.rhsHeadOrNull get() = rhs.headOrNull?.let { head -> program(name lineTo head) }
val Field.rhsTailOrNull get() = rhs.tailOrNull?.let { tail -> program(name lineTo tail) }

fun Program.make(name: String) = program(name lineTo this)
val Program._this get() = make(thisName)
