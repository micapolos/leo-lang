package leo14.type

import leo13.Stack

sealed class Reference
data class TypeReference(val type: Type) : Reference()
data class IntReference(val int: Int) : Reference()
data class RecursiveReference(val recursive: Recursive) : Reference()

sealed class Type
object NativeType : Type()
data class StructureType(val structure: Structure) : Type()
data class ListType(val list: List) : Type()
data class ChoiceType(val choice: Choice) : Type()
data class ActionType(val action: Action) : Type()

data class Structure(val fieldStack: Stack<Field>)
data class List(val field: Field)
data class Choice(val fieldStack: Stack<Field>)
data class Action(val lhs: Reference, val rhs: Reference)
data class Field(val name: String, val rhs: Reference)
data class Recursive(val type: Type)
