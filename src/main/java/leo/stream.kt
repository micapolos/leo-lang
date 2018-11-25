package leo

import leo.base.*

fun <V> Stream<V>.reflect(key: Word, reflectValue: V.() -> Term<Nothing>): FieldsTerm<Nothing> =
	key.fieldTo(reflectValue(first)).onlyStack.fold(nextOrNull) { field ->
		push(key fieldTo reflectValue(field))
	}.fieldsTerm

fun <V> Stream<V>.reflect(reflectValue: V.() -> Field<Nothing>): FieldsTerm<Nothing> =
	reflectValue(first).onlyStack.fold(nextOrNull) { field ->
		push(reflectValue(field))
	}.fieldsTerm
