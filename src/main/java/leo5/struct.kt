package leo5

import leo.base.Empty
import leo.base.empty
import leo.base.fold
import leo.base.notNullAnd

sealed class Struct

data class EmptyStruct(val empty: Empty) : Struct()
data class ExtensionStruct(val extension: StructExtension) : Struct()

fun struct(empty: Empty): Struct = EmptyStruct(empty)
fun struct(plus: StructExtension): Struct = ExtensionStruct(plus)

fun Struct.extend(field: Field) = struct(extension(this, field))
fun struct(vararg fields: Field) = struct(empty).fold(fields, Struct::extend)

fun Struct.contains(value: Value): Boolean = when (this) {
	is EmptyStruct -> value.isEmpty
	is ExtensionStruct -> value.scriptOrNull?.applicationOrNull.notNullAnd { extension.contains(it) }
}