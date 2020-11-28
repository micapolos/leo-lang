package leo23.type

import kotlinx.collections.immutable.PersistentList
import leo13.Stack

sealed class Type
object BooleanType : Type()
object TextType : Type()
object NumberType : Type()
data class ArrowType(val paramTypes: List<Type>, val returnType: Type) : Type()
data class StructType(val name: String, val fields: PersistentList<Type>) : Type()
data class ChoiceType(val name: String, val cases: PersistentList<Type>) : Type()

data class Types(val types: Stack<Type>)
