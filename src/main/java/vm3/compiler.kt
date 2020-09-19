package vm3

import vm3.dsl.layout.offset
import vm3.dsl.type.i32
import java.io.ByteArrayOutputStream

data class Compiler(
	var dataOutputStream: ByteArrayOutputStream = ByteArrayOutputStream(),
	val codeOutputStream: ByteArrayOutputStream = ByteArrayOutputStream(),
	val valueOffsets: MutableMap<Value, Offset> = HashMap(),
	val types: Types = Types(),
	val layouts: Layouts = Layouts()
)

val Fn.compiled: Compiled get() = compile(this)

fun compile(fn: Fn): Compiled {
	val compiler = Compiler()
	compiler.types[Value.Input] = fn.input
	compiler.dataHole(fn.input.size)
	val outputOffset = compiler.offset(fn.output)
	compiler.codeOutputStream.writeOp(x00_returnOpcode)
	return Compiled(
		compiler.codeOutputStream.toByteArray(),
		compiler.dataOutputStream.size(),
		compiler.type(fn.output),
		outputOffset)
}

fun Compiler.offset(value: Value): Offset =
	valueOffsets.get(value) { compileOffset(value) }

fun Compiler.pointerOffset(value: Value): Offset =
	indirectOffset(offset(value))

fun Compiler.indirectOffset(offset: Offset): Offset =
	Offset.Indirect(indirectIndex(offset))

fun Compiler.index(offset: Offset): Int =
	when (offset) {
		is Offset.Direct -> offset.index
		is Offset.Indirect -> dataHole(4).also { index ->
			codeOutputStream.writeOp(x09_set32Opcode)
			codeOutputStream.writeInt(index)
			codeOutputStream.writeInt(offset.index)
		}
	}

fun Compiler.indirectIndex(offset: Offset): Int =
	when (offset) {
		is Offset.Direct -> indirect(offset.index)
		is Offset.Indirect -> offset.index
	}

fun Compiler.indirect(directIndex: Int): Int =
	dataHole(4).also { index ->
		codeOutputStream.writeOp(x08_setConst32Opcode)
		codeOutputStream.writeInt(index)
		codeOutputStream.writeInt(directIndex)
	}

fun Compiler.direct(index: Int): Int =
	dataHole(4).also { dst ->
		codeOutputStream.writeOp(x09_set32Opcode)
		codeOutputStream.writeInt(dst)
		codeOutputStream.writeInt(index)
	}

fun Compiler.compileOffset(value: Value): Offset =
	when (value) {
		is Value.Input -> Offset.Direct(0)

		is Value.Bool -> constOffset(value.boolean.int)
		is Value.I32 -> constOffset(value.int)
		is Value.F32 -> constOffset(value.float.int)

		is Value.Array -> TODO()
		is Value.Struct -> TODO()

		is Value.ArrayAt -> add(value)
		is Value.StructAt -> add(value)

		is Value.Inc ->
			when (type(value)) {
				Type.I32 -> addOp(x10_i32IncOpcode, Type.I32, value.lhs)
				else -> TODO()
			}
		is Value.Dec ->
			when (type(value)) {
				Type.I32 -> addOp(x11_i32DecOpcode, Type.I32, value.lhs)
				else -> TODO()
			}
		is Value.Plus ->
			when (type(value)) {
				Type.I32 -> addOp(x16_i32PlusOpcode, Type.I32, value.lhs, value.rhs)
				Type.F32 -> addOp(x33_f32PlusOpcode, Type.F32, value.lhs, value.rhs)
				else -> TODO()
			}
		is Value.Minus ->
			when (type(value)) {
				Type.I32 -> addOp(x17_i32MinusOpcode, Type.I32, value.lhs, value.rhs)
				Type.F32 -> addOp(x34_f32MinusOpcode, Type.F32, value.lhs, value.rhs)
				else -> TODO()
			}
	}

fun Compiler.constOffset(lhs: Int): Offset =
	dataHole(4).let { dst ->
		codeOutputStream.writeOp(x08_setConst32Opcode)
		codeOutputStream.writeInt(dst)
		codeOutputStream.writeInt(lhs)
		Offset.Direct(dst)
	}

fun Compiler.add(structAt: Value.StructAt): Offset =
	offset(structAt.lhs).let { lhsOffset ->
		when (lhsOffset) {
			is Offset.Direct ->
				Offset.Direct(lhsOffset.index + layout(type(structAt.lhs)).offset(structAt.name))
			is Offset.Indirect -> dataHole(4).let { dst ->
				codeOutputStream.writeOp(x0A_setOffsetOpcode)
				codeOutputStream.writeInt(dst)
				codeOutputStream.writeInt(lhsOffset.index)
				codeOutputStream.writeInt(layout(type(structAt.lhs)).offset(structAt.name))
				Offset.Indirect(dst)
			}
		}
	}

fun Compiler.add2(structAt: Value.StructAt): Offset =
	pointerOffset(structAt.lhs).let { structOffset ->
		constOffset(layout(type(structAt.lhs)).offset(structAt.name)).let { fieldOffset ->
			addOp(x16_i32PlusOpcode, i32, structOffset, fieldOffset)
		}
	}

fun Compiler.add(arrayAt: Value.ArrayAt): Offset =
	indirectIndex(offset(arrayAt.lhs)).let { lhs ->
		index(offset(arrayAt.index)).let { index ->
			dataHole(4).let { dst ->
				codeOutputStream.writeOp(x0B_setIndexedOpcode)
				codeOutputStream.writeInt(dst)
				codeOutputStream.writeInt(lhs)
				codeOutputStream.writeInt(index)
				codeOutputStream.writeInt(layout(type(arrayAt)).size)
				Offset.Indirect(dst)
			}
		}
	}

fun Compiler.addOp(op: Int, type: Type, lhs: Value): Offset =
	addOp(op, type, offset(lhs))

fun Compiler.addOp(op: Int, type: Type, lhs: Offset): Offset =
	index(lhs).let { lhs ->
		dataHole(type.size).let { dst ->
			codeOutputStream.writeOp(op)
			codeOutputStream.writeInt(dst)
			codeOutputStream.writeInt(lhs)
			Offset.Direct(dst)
		}
	}

fun Compiler.addOp(op: Int, type: Type, lhs: Value, rhs: Value): Offset =
	addOp(op, type, offset(lhs), offset(rhs))

fun Compiler.addOp(op: Int, type: Type, lhs: Offset, rhs: Offset): Offset =
	index(lhs).let { lhs ->
		index(rhs).let { rhs ->
			dataHole(type.size).let { dst ->
				codeOutputStream.writeOp(op)
				codeOutputStream.writeInt(dst)
				codeOutputStream.writeInt(lhs)
				codeOutputStream.writeInt(rhs)
				Offset.Direct(dst)
			}
		}
	}

fun Compiler.type(value: Value): Type =
	types.get(value)

fun Compiler.layout(type: Type): Layout =
	layouts.get(type)

fun Compiler.dataHole(size: Int): Int =
	dataOutputStream.writeHole(size)
