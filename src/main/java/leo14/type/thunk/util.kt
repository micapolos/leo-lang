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

val TypeThunk.listThunkOrNull
	get() =
		type.listOrNull?.with(scope)

val StructureThunk.onlyFieldThunkOrNull
	get() =
		structure.onlyFieldOrNull?.with(scope)

val StructureThunk.previousThunkOrNull
	get() =
		structure.previousOrNull?.with(scope)

val StructureThunk.lastFieldThunkOrNull
	get() =
		structure.lastFieldOrNull?.with(scope)

val StructureThunk.isEmpty
	get() =
		structure.isEmpty

val FieldThunk.rhsThunk
	get() =
		field.rhs.thunk(scope)

val ReferenceThunk.typeThunk
	get() =
		reference.thunk(scope)

val TypeThunk.recurse
	get() =
		type.with(scope.plus(type))

fun TypeThunk.make(name: String) =
	type(name fieldTo type) with scope

val FieldThunk.structureThunk
	get() =
		structure(field) with scope

val StructureThunk.typeThunk
	get() =
		type(structure) with scope