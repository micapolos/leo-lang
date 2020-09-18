package vm3

import java.io.ByteArrayOutputStream

data class Compiler(
	val dataOutputStream: ByteArrayOutputStream = ByteArrayOutputStream(),
	val opOutputStream: ByteArrayOutputStream = ByteArrayOutputStream(),
	val valueIndices: MutableMap<Value, Int> = HashMap(),
	val inputType: Type
)

val Fn.compiled: Compiled get() = compile(this)

fun compile(fn: Fn): Compiled {
	val compiler = Compiler(inputType = fn.input)
	compiler.dataOutputStream.writeHole(fn.input.size)
	val outputIndex = compiler.index(fn.output)
	compiler.opOutputStream.writeOp(0x00)
	return Compiled(
		compiler.opOutputStream.toByteArray(),
		compiler.dataOutputStream.size())
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
			when (value.type(inputType)) {
				Type.I32 -> addOp(0x10, Type.I32, value.lhs)
				else -> TODO()
			}
		is Value.Dec ->
			when (value.type(inputType)) {
				Type.I32 -> addOp(0x11, Type.I32, value.lhs)
				else -> TODO()
			}
		is Value.Plus ->
			when (value.type(inputType)) {
				Type.I32 -> addOp(0x16, Type.I32, value.lhs, value.rhs)
				Type.F32 -> addOp(0x33, Type.F32, value.lhs, value.rhs)
				else -> TODO()
			}
		is Value.Minus ->
			when (value.type(inputType)) {
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
