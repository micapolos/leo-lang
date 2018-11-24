package leo

import leo.base.Stream
import leo.base.fold
import leo.base.nextOrNull

fun <V> Stream<V>.reflect(reflectValue: V.() -> Field<Nothing>): FieldsTerm<Nothing> =
	reflectValue(first).term.fold(nextOrNull) { field ->
		fieldsPush(reflectValue(field))
	}
