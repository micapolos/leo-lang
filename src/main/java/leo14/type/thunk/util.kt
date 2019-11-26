package leo14.type.thunk

import leo14.type.*

operator fun StructureThunk.plus(field: Field) =
	structure.plus(field).with(scope)

val StructureThunk.split
	get() =
		structure.split?.run { first.with(scope) to second.with(scope) }

val TypeThunk.structureThunkOrNull
	get() =
		type.structureOrNull?.with(scope)

val StructureThunk.onlyFieldThunkOrNull
	get() =
		structure.onlyFieldOrNull?.with(scope)

val StructureThunk.previousThunkOrNull
	get() =
		structure.previousOrNull?.with(scope)

val StructureThunk.lastFieldThunkOrNull
	get() =
		structure.lastFieldOrNull?.with(scope)

val FieldThunk.rhsThunk
	get() =
		field.rhs.thunk(scope)

val ReferenceThunk.typeThunk
	get() =
		reference.thunk(scope)