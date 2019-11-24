package leo14.type

import leo13.Stack

data class Scope(val typeStack: Stack<Type>)

data class TypeThunk(val type: Type, val scope: Scope)
data class NativeThunk(val scope: Scope)
data class StructureThunk(val structure: Structure, val scope: Scope)
data class ChoiceThunk(val choice: Choice, val scope: Scope)
data class ActionThunk(val action: Action, val scope: Scope)
data class RecursiveThunk(val recursive: Recursive, val scope: Scope)
data class FieldThunk(val field: Field, val scope: Scope)
data class OptionThunk(val option: Option, val scope: Scope)

infix fun Type.with(scope: Scope) = TypeThunk(this, scope)
fun nativeWith(scope: Scope) = NativeThunk(scope)
infix fun Structure.with(scope: Scope) = StructureThunk(this, scope)
infix fun Choice.with(scope: Scope) = ChoiceThunk(this, scope)
infix fun Action.with(scope: Scope) = ActionThunk(this, scope)
infix fun Recursive.with(scope: Scope) = RecursiveThunk(this, scope)
infix fun Field.with(scope: Scope) = FieldThunk(this, scope)
infix fun Option.with(scope: Scope) = OptionThunk(this, scope)
