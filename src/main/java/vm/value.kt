package vm

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import leo.base.failIfOr

data class Value(val type: Type, val op: Op)

sealed class Op
data class LocalOp(val index: Int) : Op()
data class NativeOp(val native: Any) : Op()
data class FieldAccessOp(val lhs: Value, val name: String) : Op()
data class ArrayAccessOp(val lhs: Value, val index: Value) : Op()
data class ArrayOp(val values: PersistentList<Value>) : Op()
data class StructOp(val fields: PersistentList<OpField>) : Op()
data class SwitchOp(val lhs: Value, val cases: PersistentList<Value>) : Op()
data class ChoiceOp(val index: Int, val op: Value) : Op()

data class OpField(val name: String, val value: Value)

infix fun String.of(value: Value) = OpField(this, value)

fun value(native: Any): Value =
	Value(
		type(native::class.java),
		NativeOp(native))

fun value(vararg fields: OpField): Value =
	Value(
		StructType(fields.map { it.name of it.value.type }.toPersistentList()),
		StructOp(persistentListOf(*fields)))

fun array(vararg expressions: Value): Value =
	if (expressions.isEmpty()) Value(ArrayType(type(Unit::class.java), 0), ArrayOp(persistentListOf()))
	else failIfOr(!expressions.all { it.type == expressions[0].type }) {
		Value(
			ArrayType(expressions[0].type, expressions.size),
			ArrayOp(persistentListOf(*expressions)))
	}

operator fun Value.get(name: String): Value =
	Value(
		(type as StructType).get(name),
		FieldAccessOp(this, name))

operator fun Value.get(index: Value): Value =
	Value(
		(type as ArrayType).elementType,
		ArrayAccessOp(this, index))
