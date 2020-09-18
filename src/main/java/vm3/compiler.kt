package vm3

import java.io.ByteArrayOutputStream

data class Compiler(
	val dataOutputStream: ByteArrayOutputStream = ByteArrayOutputStream(),
	val opOutputStream: ByteArrayOutputStream = ByteArrayOutputStream(),
	val valueIndices: MutableMap<Value, Int> = HashMap()
)

fun compile(input: Type, output: Value): Compiled {
	val compiler = Compiler()
	compiler.dataOutputStream.writeHole(input.size)
	val outputIndex = compiler.index(output)
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

		is Value.I32Inc ->
			addOp(0x10, Type.I32, value.lhs)
		is Value.I32Dec ->
			addOp(0x11, Type.I32, value.lhs)
		is Value.I32UnaryMinus ->
			addOp(0x12, Type.I32, value.lhs)
		is Value.I32Plus ->
			addOp(0x16, Type.I32, value.lhs, value.rhs)
		is Value.I32Minus ->
			addOp(0x17, Type.I32, value.lhs, value.rhs)
		is Value.I32Times ->
			addOp(0x18, Type.I32, value.lhs, value.rhs)
		is Value.I32Div ->
			addOp(0x19, Type.I32, value.lhs, value.rhs)

		is Value.I32Inv ->
			addOp(0x1B, Type.I32, value.lhs)
		is Value.I32And ->
			addOp(0x1D, Type.I32, value.lhs, value.rhs)
		is Value.I32Or ->
			addOp(0x1E, Type.I32, value.lhs, value.rhs)
		is Value.I32Xor ->
			addOp(0x1F, Type.I32, value.lhs, value.rhs)

		is Value.I32IsZero ->
			addOp(0x28, Type.Bool, value.lhs)
		is Value.I32Eq ->
			addOp(0x29, Type.Bool, value.lhs, value.rhs)
		is Value.I32Neq ->
			addOp(0x2A, Type.Bool, value.lhs, value.rhs)
		is Value.I32Lt ->
			addOp(0x2B, Type.Bool, value.lhs, value.rhs)
		is Value.I32Gt ->
			addOp(0x2C, Type.Bool, value.lhs, value.rhs)
		is Value.I32Le ->
			addOp(0x2D, Type.Bool, value.lhs, value.rhs)
		is Value.I32Ge ->
			addOp(0x2E, Type.Bool, value.lhs, value.rhs)

		is Value.F32Plus ->
			addOp(0x33, Type.F32, value.lhs, value.rhs)
		is Value.F32Minus ->
			addOp(0x34, Type.F32, value.lhs, value.rhs)
		is Value.F32Times ->
			addOp(0x35, Type.F32, value.lhs, value.rhs)
		is Value.F32Div ->
			addOp(0x36, Type.F32, value.lhs, value.rhs)

		is Value.F32IsZero ->
			addOp(0x40, Type.Bool, value.lhs)
		is Value.F32Equals ->
			addOp(0x41, Type.Bool, value.lhs, value.rhs)
		is Value.F32NotEquals ->
			addOp(0x42, Type.Bool, value.lhs, value.rhs)
		is Value.F32IsLessThan ->
			addOp(0x43, Type.Bool, value.lhs, value.rhs)
		is Value.F32IsGreaterThan ->
			addOp(0x44, Type.Bool, value.lhs, value.rhs)
		is Value.F32IsLessOrEqualTo ->
			addOp(0x45, Type.Bool, value.lhs, value.rhs)
		is Value.F32IfGreaterOrEqualTo ->
			addOp(0x46, Type.Bool, value.lhs, value.rhs)

		is Value.F32Sin -> TODO()
		is Value.F32Cos -> TODO()
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
