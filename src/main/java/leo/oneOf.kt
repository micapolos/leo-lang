package leo

import leo.base.*

data class OneOf<out V>(
	val valueStack: Stack<V>) {
	//override fun toString() = reflect.string
}

val <V> Stack<V>.oneOf: OneOf<V>
	get() =
		OneOf(this)

fun <V> oneOf(value: V, vararg values: V): OneOf<V> =
	stack(value, *values).oneOf

val <V> OneOf<V>.valueStream: Stream<V>
	get() =
		valueStack.reverse.stream

// === contains

fun <V : Any> OneOf<V>.contains(value: V): Boolean =
	valueStack.contains(value)

// === reflect

val OneOf<Nothing>.reflect: Field<Nothing>
	get() =
		reflect { fail }

fun <V> OneOf<V>.reflect(valueReflect: V.() -> Term<Nothing>): Field<Nothing> =
	oneWord fieldTo term(
		ofWord fieldTo valueStream.reflect(theWord, valueReflect))

// === parse

fun <V : Any> Field<Nothing>.parseOneOf(valueParse: Term<Nothing>.() -> V?): OneOf<V>? =
	matchKey(oneWord) {
		matchFieldKey(ofWord) {
			parseStack { matchKey(theWord, valueParse) }
		}
	}?.oneOf
