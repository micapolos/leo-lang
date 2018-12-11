package leo.term

import leo.Word
import leo.base.nullOf

data class Application<out V>(
	val left: Left<V>,
	val operator: Operator,
	val right: Right<V>)

fun <V> Left<V>.apply(operator: Operator, right: Right<V>): Application<V> =
	Application(this, operator, right)

fun <V> Term<V>?.apply(word: Word, termOrNull: Term<V>?): Application<V> =
	left.apply(word.operator, termOrNull.right)

val <V> Line<V>.application: Application<V>
	get() =
		nullOf<Term<V>>().left.apply(operator, right)

fun <V> Application<V>.plus(line: Line<V>): Application<V> =
	Application(term.left, line.operator, line.right)

fun <V> Appendable.append(application: Application<V>): Appendable =
	this
		.append(application.left)
		.append(application.operator)
		.append(application.right)

fun <V, R> Application<V>.map(fn: V.() -> R): Application<R> =
	left.map(fn).apply(operator, right.map(fn))