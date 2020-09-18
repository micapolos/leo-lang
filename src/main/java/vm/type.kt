package vm

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

sealed class Type
data class NativeType(val native: Any) : Type()
data class IndexType(var size: Int) : Type()
data class StructType(val fields: PersistentList<Field>) : Type()
data class ChoiceType(val fields: PersistentList<Field>) : Type()
data class ArrayType(val elementType: Type, val size: Int) : Type()

data class Field(val name: String, val type: Type)

infix fun String.of(type: Type) = Field(this, type)

fun type(native: Any): Type = NativeType(native)
fun struct(vararg fields: Field): Type = StructType(persistentListOf(*fields))
fun choice(vararg fields: Field): Type = ChoiceType(persistentListOf(*fields))
operator fun Type.get(size: Int): Type = ArrayType(this, size)

val int = type(Integer::class.java)
val float = type(java.lang.Float::class.java)
val string = type(String::class.java)

operator fun Type.get(name: String): Type =
	(this as StructType).fields.first { it.name == name }.type
