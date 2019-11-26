package leo14.type.thunk

import leo14.type.Field
import leo14.type.plus
import leo14.type.split

operator fun StructureThunk.plus(field: Field) =
	structure.plus(field).with(scope)

val StructureThunk.split
	get() =
		structure.split?.run { first.with(scope) to second.with(scope) }