package leo.lab

import leo.base.*

fun <V> Stack<V>.reflect(reflectValue: (V) -> Field<Unit>): Term<Unit> =
	reverse.stream
		.foldFirst { value -> reflectValue(value).onlyStack }
		.foldNext { value -> push(reflectValue(value)) }
		.term

