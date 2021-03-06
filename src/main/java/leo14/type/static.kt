package leo14.type

import leo13.all
import leo14.type.thunk.*

val Type.isStatic get() = with(scope()).isStatic

val TypeThunk.isStatic: Boolean
	get() =
		when (type) {
			is NativeType -> type.native.isStatic
			is StructureType -> type.structure.with(scope).isStatic
			is ListType -> false
			is ChoiceType -> false
			is ActionType -> type.action.with(scope).isStatic
	}

val StructureThunk.isStatic
	get() =
		structure.fieldStack.all { with(scope).isStatic }

val FieldThunk.isStatic
	get() =
		field.rhs.with(scope).isStatic

val ActionThunk.isStatic
	get() =
		action.lhs.thunk(scope).isStatic || action.rhs.thunk(scope).isStatic

val ReferenceThunk.isStatic get() =
	when (reference) {
		is TypeReference -> typeThunk.isStatic
		is IntReference -> typeThunk.isStatic
		is RecursiveReference -> false
	}