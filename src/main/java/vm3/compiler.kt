package vm3

import java.io.ByteArrayOutputStream

data class Compiler(
	val dataOutputStream: ByteArrayOutputStream = ByteArrayOutputStream(),
	val opOutputStream: ByteArrayOutputStream = ByteArrayOutputStream(),
	val valueIndices: MutableMap<Value, Int> = HashMap(),
	val valueTypes: MutableMap<Value, Type> = HashMap()
)

val Fn.compiled: Compiled get() = compile(this)

fun compile(fn: Fn): Compiled {
	val compiler = Compiler()
	compiler.valueTypes[Value.Input] = fn.input
	compiler.dataOutputStream.writeHole(fn.input.size)
	val outputIndex = compiler.index(fn.output)
	compiler.opOutputStream.writeOp(0x00)
	return Compiled(
		compiler.opOutputStream.toByteArray(),
		compiler.dataOutputStream.size(),
		compiler.type(fn.output),
		outputIndex)
}

fun Compiler.index(value: Value): Int {
	val index = valueIndices.get(value)
	return if (index != null) index
	else {
		val index = add(value)
		valueIndices[value] = index
		index
	}
}

fun Compiler.add(value: Value): Int =
	when (value) {
		is Value.Input -> 0

		is Value.Bool -> addConst(value.boolean.int)
		is Value.I32 -> addConst(value.int)
		is Value.F32 -> addConst(value.float.int)

		is Value.Struct -> TODO()
		is Value.Array -> TODO()
		is Value.StructAt -> TODO()
		is Value.ArrayAt -> TODO()

		is Value.Inc ->
			when (type(value)) {
				Type.I32 -> addOp(0x10, Type.I32, value.lhs)
				else -> TODO()
			}
		is Value.Dec ->
			when (type(value)) {
				Type.I32 -> addOp(0x11, Type.I32, value.lhs)
				else -> TODO()
			}
		is Value.Plus ->
			when (type(value)) {
				Type.I32 -> addOp(0x16, Type.I32, value.lhs, value.rhs)
				Type.F32 -> addOp(0x33, Type.F32, value.lhs, value.rhs)
				else -> TODO()
			}
		is Value.Minus ->
			when (type(value)) {
				Type.I32 -> addOp(0x17, Type.I32, value.lhs, value.rhs)
				Type.F32 -> addOp(0x34, Type.F32, value.lhs, value.rhs)
				else -> TODO()
			}
	}


fun Compiler.addConst(lhs: Int): Int =
	dataOutputStream.writeHole(4).also { dst ->
		opOutputStream.writeByte(0x08)
		opOutputStream.writeInt(dst)
		opOutputStream.writeInt(lhs)
	}

fun Compiler.addOp(op: Byte, type: Type, lhs: Value): Int =
	index(lhs).let { lhs ->
		dataOutputStream.writeHole(type.size).also { dst ->
			opOutputStream.writeByte(op)
			opOutputStream.writeInt(dst)
			opOutputStream.writeInt(lhs)
		}
	}

fun Compiler.addOp(op: Byte, type: Type, lhs: Value, rhs: Value): Int =
	index(lhs).let { lhs ->
		index(rhs).let { rhs ->
			dataOutputStream.writeHole(type.size).also { dst ->
				opOutputStream.writeByte(op)
				opOutputStream.writeInt(dst)
				opOutputStream.writeInt(lhs)
				opOutputStream.writeInt(rhs)
			}
		}
	}

fun Compiler.type(value: Value): Type {
	val type = valueTypes.get(value)
	return if (type != null) type
	else {
		val type = compileType(value)
		valueTypes[value] = type
		type
	}
}

fun Compiler.compileType(value: Value): Type =
	when (value) {
		Value.Input -> error("unknown type")
		is Value.Bool -> Type.Bool
		is Value.I32 -> Type.I32
		is Value.F32 -> Type.F32
		is Value.Struct -> Type.Struct(value.fields.map { Type.Struct.Field(it.name, type(it.value)) })
		is Value.Array -> Type.Array(type(value.values[0]), value.values.size)
		is Value.ArrayAt -> TODO()
		is Value.StructAt -> TODO()
		is Value.Inc ->
			when (type(value.lhs)) {
				Type.I32 -> Type.I32
				else -> null
			}
		is Value.Dec ->
			when (type(value.lhs)) {
				Type.I32 -> Type.I32
				else -> null
			}
		is Value.Plus ->
			when (type(value.lhs)) {
				Type.I32 ->
					when (type(value.lhs)) {
						Type.I32 -> Type.I32
						else -> null
					}
				Type.F32 ->
					when (type(value.lhs)) {
						Type.F32 -> Type.F32
						else -> null
					}
				else -> null
			}
		is Value.Minus ->
			when (type(value.lhs)) {
				Type.I32 ->
					when (type(value.lhs)) {
						Type.I32 -> Type.I32
						else -> null
					}
				Type.F32 ->
					when (type(value.lhs)) {
						Type.F32 -> Type.F32
						else -> null
					}
				else -> null
			}
	} ?: error("type($value)")
