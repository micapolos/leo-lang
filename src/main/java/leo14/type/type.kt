package leo14.type

import leo13.Index
import leo13.Stack

data class Scope(val typeStack: Stack<Type>)

sealed class Reference
data class TypeReference(val type: Type) : Reference()
data class IndexReference(val index: Index) : Reference()

sealed class Type
object NativeType : Type()
object EmptyType : Type()
data class StructureType(val structure: Structure) : Type()
data class ChoiceType(val choice: Choice) : Type()
data class ActionType(val action: Action) : Type()
data class RecursiveType(val recursive: Recursive) : Type()

data class Recursive(val type: Type)
data class Structure(val fieldStack: Stack<Field>)
data class Choice(val optionStack: Stack<Option>)
data class Action(val lhs: Reference, val rhs: Reference)
data class Field(val name: String, val reference: Reference)
data class Option(val name: String, val reference: Reference)
