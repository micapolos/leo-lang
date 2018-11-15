package leo.lab

import leo.Word
import leo.base.*
import leo.stackWord

fun <V> Stack<V>.reflect(key: Word, reflectValue: (V) -> Field<Nothing>): Field<Nothing> =
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
