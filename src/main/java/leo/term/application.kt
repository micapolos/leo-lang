package leo.term

import leo.Word
import leo.base.ifNotNull
import leo.base.nullOf

data class Application<out V>(
	val leftOrNull: Left<V>?,
	val operator: Operator,
	val rightOrNull: Right<V>?)

fun <V> Left<V>?.apply(operator: Operator, rightOrNull: Right<V>?): Application<V> =
	Application(this, operator, rightOrNull)

fun <V> Term<V>?.apply(word: Word, termOrNull: Term<V>?): Application<V> =
	this?.operand?.left.apply(word.operator, termOrNull?.operand?.right)

val <V> Line<V>.application: Application<V>
	get() =
		nullOf<Left<V>>().apply(operator, rightOrNull)

fun <V> Application<V>.plus(line: Line<V>): Application<V> =
	Application(term.operand.left, line.operator, line.rightOrNull)

fun <V> Appendable.append(application: Application<V>): Appendable =
	this
		.ifNotNull(application.leftOrNull) { left ->
			append(left).append(", ")
		}
		.append(application.operator)
		.ifNotNull(application.rightOrNull) { right ->
			if (right.isSimple) append(" ").append(right)
			else append("(").append(right).append(")")
		}


fun <V, R> Application<V>.map(fn: V.() -> R): Application<R> =
	leftOrNull?.map(fn).apply(operator, rightOrNull?.map(fn))