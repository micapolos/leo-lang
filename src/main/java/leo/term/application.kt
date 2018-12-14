package leo.term

import leo.Word
import leo.append
import leo.base.ifNotNull
import leo.base.string

data class Application<out V : Any>(
	val word: Word,
	val argumentOrNull: V?)

fun <V : Any> Word.application(): Application<V> =
	Application(this, null)

infix fun <V : Any> Word.apply(argumentOrNull: V?): Application<V> =
	Application(this, argumentOrNull)

fun <V : Any> Appendable.append(application: Application<V>): Appendable =
	this
		.append(application.word)
		.ifNotNull(application.argumentOrNull) { argument ->
			append('(').append(argument.string).append(')')
		}

val <V : Any> Application<V>.fieldOrNull: Field<V>?
	get() =
		argumentOrNull?.let { argument -> word fieldTo argument }

val <V : Any> Application<V>.term: Term<V>
	get() =
		Term(null, this)
