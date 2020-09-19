package vm3

import vm3.dsl.layout.offset
import vm3.dsl.type.i32
import java.io.ByteArrayOutputStream

data class Compiler(
	var dataSize: Int = 0,
	val codeOutputStream: ByteArrayOutputStream = ByteArrayOutputStream(),
	val valueOffsets: MutableMap<Value, Offset> = HashMap(),
	val types: Types = Types(),
	val layouts: Layouts = Layouts()
)

val Fn.compiled: Compiled get() = compile(this)

fun compile(fn: Fn): Compiled {
	val compiler = Compiler()
	compiler.types[Value.Input] = fn.input
	val outputType = compiler.type(fn.output)
	compiler.dataHole(fn.input.size)
	val outputOffset = compiler.offset(fn.output)
	compiler.codeOutputStream.writeOp(x00_returnOpcode)
	return Compiled(
		compiler.codeOutputStream.toByteArray(),
		compiler.dataSize,
		outputType,
		outputOffset)
}

fun Compiler.offset(value: Value): Offset =
	value.optimize.let { value ->
		valueOffsets.get(value) { compileOffset(value) }
	}

fun Compiler.pointerOffset(value: Value): Offset =
	Offset.Direct(indirectIndex(offset(value)))

fun Compiler.depointerOffset(offset: Offset): Offset =
	when (offset) {
		is Offset.Direct -> Offset.Indirect(offset.index)
		is Offset.Indirect -> TODO()
	}

fun Compiler.indirectOffset(offset: Offset): Offset =
	Offset.Direct(indirectIndex(offset))

fun Compiler.index(offset: Offset): Int =
	when (offset) {
		is Offset.Direct -> offset.index
		is Offset.Indirect -> dataHole(4).also { index ->
			codeOutputStream.writeOp(x0A_setIndirect32Opcode)
			codeOutputStream.writeInt(index)
			codeOutputStream.writeInt(offset.index)
		}
	}

fun Compiler.index(value: Value): Int =
	index(offset(value))

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

		is Value.Array -> add(value)
		is Value.Struct -> add(value)

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
		is Value.Times ->
			when (type(value)) {
				Type.I32 -> addOp(x18_i32TimesOpcode, Type.I32, value.lhs, value.rhs)
				Type.F32 -> addOp(x35_f32TimesOpcode, Type.F32, value.lhs, value.rhs)
				else -> TODO()
			}
	}

fun Compiler.set(dst: Int, value: Value) {
	when (value) {
		is Value.Input -> TODO() // traverse the layout or memcpy?

		is Value.Bool -> setConst32(dst, value.boolean.int)
		is Value.I32 -> setConst32(dst, value.int)
		is Value.F32 -> setConst32(dst, value.float.int)

		is Value.Array -> set(dst, value)
		is Value.Struct -> set(dst, value)

		is Value.ArrayAt -> TODO()
		is Value.StructAt -> TODO()

		is Value.Inc ->
			when (type(value)) {
				Type.I32 -> setOp(x10_i32IncOpcode, dst, value.lhs)
				else -> TODO()
			}
		is Value.Dec ->
			when (type(value)) {
				Type.I32 -> setOp(x11_i32DecOpcode, dst, value.lhs)
				else -> TODO()
			}
		is Value.Plus ->
			when (type(value)) {
				Type.I32 -> setOp(x16_i32PlusOpcode, dst, value.lhs)
				Type.F32 -> setOp(x33_f32PlusOpcode, dst, value.lhs)
				else -> TODO()
			}
		is Value.Minus ->
			when (type(value)) {
				Type.I32 -> setOp(x17_i32MinusOpcode, dst, value.lhs)
				Type.F32 -> setOp(x34_f32MinusOpcode, dst, value.lhs)
				else -> TODO()
			}
		is Value.Times ->
			when (type(value)) {
				Type.I32 -> setOp(x18_i32TimesOpcode, dst, value.lhs)
				Type.F32 -> setOp(x35_f32TimesOpcode, dst, value.lhs)
				else -> TODO()
			}
	}
}

fun Compiler.constOffset(lhs: Int): Offset =
	dataHole(4).let { dst ->
		codeOutputStream.writeOp(x08_setConst32Opcode)
		codeOutputStream.writeInt(dst)
		codeOutputStream.writeInt(lhs)
		Offset.Direct(dst)
	}

fun Compiler.setConst32(dst: Int, src: Int) {
	codeOutputStream.writeOp(x08_setConst32Opcode)
	codeOutputStream.writeInt(dst)
	codeOutputStream.writeInt(src)
}

fun Compiler.set(dst: Int, struct: Value.Struct) {
	struct.fields.zip((layout(type(struct)).body as Layout.Body.Struct).fields).map { (field, layout) ->
		set(dst + layout.offset, field.value)
	}
}

fun Compiler.set(dst: Int, array: Value.Array) {
	(layout(type(array)).body as Layout.Body.Array).let { layout ->
		array.items.mapIndexed { index, value ->
			set(dst + index * layout.itemLayout.size, value)
		}
	}
}

fun Compiler.add(size: Int, setFn: (Int) -> Unit): Offset =
	dataHole(size).let { dst ->
		setFn(dst)
		Offset.Direct(dst)
	}


fun Compiler.add(struct: Value.Struct): Offset =
	add(size(struct)) { dst -> set(dst, struct) }

fun Compiler.add(array: Value.Array): Offset =
	add(size(array)) { dst -> set(dst, array) }

fun Compiler.add(structAt: Value.StructAt): Offset =
	pointerOffset(structAt.lhs).let { structOffset ->
		constOffset(layout(type(structAt.lhs)).offset(structAt.name)).let { fieldOffset ->
			depointerOffset(addOp(x16_i32PlusOpcode, i32, structOffset, fieldOffset))
		}
	}

fun Compiler.add(arrayAt: Value.ArrayAt): Offset =
	pointerOffset(arrayAt.lhs).let { arrayOffset ->
		offset(arrayAt.index).let { indexOffset ->
			constOffset(size(arrayAt)).let { sizeOffset ->
				addOp(x18_i32TimesOpcode, i32, indexOffset, sizeOffset).let { itemOffset ->
					depointerOffset(addOp(x16_i32PlusOpcode, i32, arrayOffset, itemOffset))
				}
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

fun Compiler.setOp(op: Int, dst: Int, lhs: Value) =
	setOp(op, dst, index(lhs))

fun Compiler.setOp(op: Int, dst: Int, lhs: Value, rhs: Value) =
	setOp(op, dst, index(lhs), index(rhs))

fun Compiler.setOp(op: Int, dst: Int, lhs: Int) {
	codeOutputStream.writeOp(op)
	codeOutputStream.writeInt(dst)
	codeOutputStream.writeInt(lhs)
}

fun Compiler.setOp(op: Int, dst: Int, lhs: Int, rhs: Int) {
	codeOutputStream.writeOp(op)
	codeOutputStream.writeInt(dst)
	codeOutputStream.writeInt(lhs)
	codeOutputStream.writeInt(rhs)
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
	dataSize.also { dataSize += size }

fun Compiler.size(value: Value): Int =
	layout(type(value)).size