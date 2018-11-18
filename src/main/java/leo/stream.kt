package leo

import leo.base.Stream
import leo.base.foldFirst
import leo.base.foldNext
import leo.base.reverse

fun <V> Stream<V>.reflect(key: Word, reflectValue: (V) -> Field<Nothing>): Field<Nothing> =
	key fieldTo term(
		stackWord fieldTo reverse
			.foldFirst { value -> reflectValue(value).term }
			.foldNext { value -> push(reflectValue(value)) })

fun <V> Stream<V>.reflect(reflectValue: V.() -> Field<Nothing>): Term<Nothing> =
	reverse
		.foldFirst { value -> reflectValue(value).term }
		.foldNext { value -> push(reflectValue(value)) }
