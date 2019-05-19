package leo3

import leo.base.*
import leo.binary.Bit

data class Value(
	val nodeOrNull: Node?,
	val scope: Scope) {
	override fun toString() = appendableString { it.append(this) }
}

val Scope.emptyValue
	get() = Value(null, this)

val Empty.value
	get() = empty.scope.emptyValue

fun value(vararg lines: Line) =
	empty.value.fold(lines) { plus(it) }

fun value(string: String) =
	value(line(word(string)))

val Value.lineSeq: Seq<Line>
	get() =
		nodeOrNull.orEmptyIfNullSeq { lineSeq }

fun Value.plus(line: Line) =
	Value(node(this, line.word, line.value), scope)

fun Value.plus(value: Value) =
	fold(value.lineSeq) { plus(it) }

fun Appendable.append(value: Value): Appendable =
	ifNotNull(value.nodeOrNull) { append(it) }

fun Value.apply(parameter: Parameter) = this

fun Value.call(line: Line) =
	scope.templateAt(line)!!.apply(parameter(plus(line)))

val Value.tokenSeq: Seq<Token>
	get() = nodeOrNull.orEmptyIfNullSeq { tokenSeq }

val Value.bitSeq: Seq<Bit>
	get() = nodeOrNull.orEmptyIfNullSeq { bitSeq }