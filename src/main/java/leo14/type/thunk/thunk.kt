package leo14.type.thunk

import leo13.Stack
import leo14.type.*
import leo14.type.List

data class Scope(val typeStack: Stack<Type>)

data class TypeThunk(val type: Type, val scope: Scope)
data class NativeThunk(val native: Native, val scope: Scope)
data class StructureThunk(val structure: Structure, val scope: Scope)
data class ListThunk(val list: List, val scope: Scope)
data class ChoiceThunk(val choice: Choice, val scope: Scope)
data class ActionThunk(val action: Action, val scope: Scope)
data class RecursiveThunk(val recursive: Recursive, val scope: Scope)
data class ReferenceThunk(val reference: Reference, val scope: Scope)
data class FieldThunk(val field: Field, val scope: Scope)

infix fun Type.with(scope: Scope) = TypeThunk(this, scope)
infix fun Native.with(scope: Scope) = NativeThunk(this, scope)
infix fun Structure.with(scope: Scope) = StructureThunk(this, scope)
infix fun List.with(scope: Scope) = ListThunk(this, scope)
infix fun Choice.with(scope: Scope) = ChoiceThunk(this, scope)
infix fun Action.with(scope: Scope) = ActionThunk(this, scope)
infix fun Recursive.with(scope: Scope) = RecursiveThunk(this, scope)
infix fun Reference.with(scope: Scope) = ReferenceThunk(this, scope)
infix fun Field.with(scope: Scope) = FieldThunk(this, scope)

infix fun Scope.with(type: Type) = type with this
