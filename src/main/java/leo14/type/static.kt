package leo14.type

import leo13.all

val Thunk.isStatic: Boolean
	get() =
		reference.isStatic(scope)

fun Reference.isStatic(scope: Scope): Boolean =
	type(scope).isStatic(scope)

val Type.isStatic: Boolean get() = isStatic(scope())

fun Type.isStatic(scope: Scope) =
	when (this) {
		NativeType -> false
		is StructureType -> structure.isStatic(scope)
		is ChoiceType -> false
		is ActionType -> action.isStatic(scope)
		is RecursiveType -> false
	}

fun Structure.isStatic(scope: Scope) =
	fieldStack.all { isStatic(scope) }

fun Field.isStatic(scope: Scope) =
	reference.isStatic(scope)

fun Action.isStatic(scope: Scope) =
	lhs.isStatic(scope) || rhs.isStatic(scope)
