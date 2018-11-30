package leo

import leo.base.*

fun <V> Stream<V>.reflect(key: Word, reflectValue: V.() -> Term<Nothing>): StructureTerm<Nothing> =
	key.fieldTo(reflectValue(first)).onlyStack.fold(nextOrNull) { field ->
		push(key fieldTo reflectValue(field))
	}.structureTerm

fun <V> Stream<V>.reflect(reflectValue: V.() -> Field<Nothing>): StructureTerm<Nothing> =
	reflectValue(first).onlyStack.fold(nextOrNull) { field ->
		push(reflectValue(field))
	}.structureTerm

fun <V> Stream<Term<V>>.termReflect(reflectValue: V.() -> Field<Nothing>): StructureTerm<Nothing> =
	reflect { theWord fieldTo reflectMeta(reflectValue) }
