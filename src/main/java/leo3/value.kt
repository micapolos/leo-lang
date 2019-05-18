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

fun value(vararg lines: Line) =
	empty.scope.emptyValue.fold(lines, Value::plus)

fun Value.plus(line: Line) =
	Value(node(this, line.word, line.value), scope)

fun Appendable.append(value: Value): Appendable =
	ifNotNull(value.nodeOrNull) { append(it) }

fun Value.apply(parameter: Parameter) = this
fun Value.apply(value: Value): Value = TODO()

val Value.tokenSeq: Seq<Token>
	get() = nodeOrNull.orEmptyIfNullSeq { tokenSeq }

val Value.bitSeq: Seq<Bit>
	get() = nodeOrNull.orEmptyIfNullSeq { bitSeq }