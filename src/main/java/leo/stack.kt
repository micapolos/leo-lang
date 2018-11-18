package leo

import leo.base.*

fun <V> Stack<V>.reflect(key: Word, reflectValue: V.() -> Field<Nothing>): Field<Nothing> =
	key fieldTo term(
		stackWord fieldTo reverse
			.foldTop { value -> reflectValue(value).onlyStack }
			.foldPop { fieldStack, value -> fieldStack.push(reflectValue(value)) }
			.term)

fun <V> Stack<V>.reflect(reflectValue: (V) -> Field<Nothing>): Term<Nothing> =
	reverse
		.foldTop { value -> reflectValue(value).onlyStack }
		.foldPop { fieldStack, value -> fieldStack.push(reflectValue(value)) }
		.term

fun <V> Term<Nothing>.parseStack(parseValue: (Field<Nothing>) -> V?): Stack<V>? =
	structureTermOrNull
		?.fieldStream
		?.reverse
		?.foldFirst { field ->
			parseValue(field)?.onlyStack
		}
		?.foldNext { field ->
			parseValue(field)?.let { value ->
				push(value)
			}
		}
