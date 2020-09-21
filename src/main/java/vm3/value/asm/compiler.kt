package vm3.value.asm

import leo.base.notNullOrError
import vm3.Layout
import vm3.Layouts
import vm3.Offset
import vm3.Type
import vm3.Types
import vm3.dsl.layout.offset
import vm3.dsl.type.i32
import vm3.get
import vm3.getOrCompute
import vm3.int
import vm3.optimize
import vm3.push
import vm3.size
import vm3.value.Value
import vm3.writeInt
import vm3.writeOp
import vm3.x00_exitOpcode
import vm3.x06_callOpcode
import vm3.x07_retOpcode
import vm3.x08_setConst32Opcode
import vm3.x09_set32Opcode
import vm3.x0A_setIndirect32Opcode
import vm3.x0B_setSizeOpcode
import vm3.x10_i32IncOpcode
import vm3.x11_i32DecOpcode
import vm3.x16_i32PlusOpcode
import vm3.x17_i32MinusOpcode
import vm3.x18_i32TimesOpcode
import vm3.x33_f32PlusOpcode
import vm3.x34_f32MinusOpcode
import vm3.x35_f32TimesOpcode
import java.io.ByteArrayOutputStream

data class Compiler(
	var dataSize: Int = 0,
	val codeOutputStream: ByteArrayOutputStream = ByteArrayOutputStream(),
	var valueOffsets: MutableMap<Value, Offset> = mutableMapOf(),
	val parameterOffsets: MutableList<Offset> = mutableListOf(),
	val compiledFunctions: MutableMap<Value.Function, CompiledFunction> = mutableMapOf(),
	val types: Types = Types(),
	val layouts: Layouts = Layouts()
)

data class CompiledFunction(
	val inputIndex: Int,
	val jumpAddress: Int,
	val retAddress: Int,
	val outputIndex: Int
)

val Value.Function.compiled: Compiled get() = compile(this)

fun compile(function: Value.Function): Compiled {
	val compiler = Compiler()
	compiler.compileFunctions(function.body)
	compiler.parameterOffsets.add(Offset.Direct(0))
	compiler.types.push(function.param)
	val outputType = compiler.type(function.body)
	compiler.dataHole(function.param.size)
	val outputOffset = compiler.offset(function.body)
	compiler.codeOutputStream.writeOp(x00_exitOpcode)
	return Compiled(
		compiler.codeOutputStream.toByteArray(),
		compiler.dataSize,
		outputType,
		outputOffset)
}

fun Compiler.offset(value: Value): Offset =
	value.optimize.let { value ->
		if (value is Value.Argument) compileOffset(value)
		else valueOffsets.getOrCompute(value) { compileOffset(value) }
	}

fun Compiler.pointerOffset(value: Value): Offset =
	Offset.Direct(indirectIndex(offset(value)))

fun Compiler.depointerOffset(offset: Offset): Offset =
	when (offset) {
		is Offset.Direct -> Offset.Indirect(offset.index)
		is Offset.Indirect -> TODO()
	}

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
		is Value.Argument -> parameterOffsets[parameterOffsets.size - 1 - value.depth]

		is Value.Bool -> add32(value.boolean.int)
		is Value.I32 -> add32(value.int)
		is Value.F32 -> add32(value.float.int)

		is Value.ArrayAt -> add(value)
		is Value.StructAt -> add(value)

		is Value.Call -> compileOffset(value)

		else -> null
	} ?: add(size(value)) { dst -> set(dst, value) }

fun Compiler.set(dst: Int, value: Value) {
	when (value) {
		is Value.Argument -> setSize(dst, index(value), size(value))

		is Value.Bool -> setConst32(dst, value.boolean.int)
		is Value.I32 -> setConst32(dst, value.int)
		is Value.F32 -> setConst32(dst, value.float.int)

		is Value.Array -> set(dst, value)
		is Value.Struct -> set(dst, value)

		is Value.ArrayAt -> TODO()
		is Value.StructAt -> TODO()

		is Value.Call -> TODO()

		is Value.Switch -> set(dst, value)

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
				Type.I32 -> setOp(x16_i32PlusOpcode, dst, value.lhs, value.rhs)
				Type.F32 -> setOp(x33_f32PlusOpcode, dst, value.lhs, value.rhs)
				else -> TODO()
			}
		is Value.Minus ->
			when (type(value)) {
				Type.I32 -> setOp(x17_i32MinusOpcode, dst, value.lhs, value.rhs)
				Type.F32 -> setOp(x34_f32MinusOpcode, dst, value.lhs, value.rhs)
				else -> TODO()
			}
		is Value.Times ->
			when (type(value)) {
				Type.I32 -> setOp(x18_i32TimesOpcode, dst, value.lhs, value.rhs)
				Type.F32 -> setOp(x35_f32TimesOpcode, dst, value.lhs, value.rhs)
				else -> TODO()
			}
	}
}

fun Compiler.add32(lhs: Int): Offset =
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

fun Compiler.set(dst: Int, switch: Value.Switch) {
	val lhsType = type(switch.lhs)
	val functionOrNull = switch
		.functions
		.findLast { lhsType == it.param }
	if (functionOrNull != null) {
		types.push(lhsType) {
			push(offset(switch.lhs)) {
				set(dst, functionOrNull.body)
			}
		}
	} else {
		TODO()
	}
}

fun Compiler.setSize(dst: Int, src: Int, size: Int) {
	codeOutputStream.writeOp(x0B_setSizeOpcode)
	codeOutputStream.writeInt(dst)
	codeOutputStream.writeInt(src)
	codeOutputStream.writeInt(size)
}

fun Compiler.compileOffset(call: Value.Call): Offset =
	compiledFunctions[call.function]
		.notNullOrError("${call.function} not compiled")
		.let { compiledFunction ->
			set(compiledFunction.inputIndex, call.param)
			emitCall(compiledFunction.jumpAddress, compiledFunction.retAddress)
			Offset.Direct(compiledFunction.outputIndex)
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
		add32(layout(type(structAt.lhs)).offset(structAt.name)).let { fieldOffset ->
			depointerOffset(addOp(x16_i32PlusOpcode, i32, structOffset, fieldOffset))
		}
	}

fun Compiler.add(arrayAt: Value.ArrayAt): Offset =
	pointerOffset(arrayAt.lhs).let { arrayOffset ->
		offset(arrayAt.index).let { indexOffset ->
			add32(size(arrayAt)).let { sizeOffset ->
				addOp(x18_i32TimesOpcode, i32, indexOffset, sizeOffset).let { itemOffset ->
					depointerOffset(addOp(x16_i32PlusOpcode, i32, arrayOffset, itemOffset))
				}
			}
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
	types[value]

fun Compiler.layout(type: Type): Layout =
	layouts[type]

fun Compiler.dataHole(size: Int): Int =
	dataSize.also { dataSize += size }

fun Compiler.size(value: Value): Int =
	size(type(value))

fun Compiler.size(type: Type): Int =
	layout(type).size

fun <T> Compiler.push(offset: Offset, fn: () -> T): T {
	parameterOffsets.add(offset)
	val result = fn()
	parameterOffsets.removeAt(parameterOffsets.size - 1)
	return result
}

fun <T> Compiler.clearValueOffsets(fn: () -> T): T {
	val oldValueOffsets = valueOffsets
	val result = fn()
	valueOffsets = oldValueOffsets
	return result
}

val Compiler.pc get() = codeOutputStream.size()

fun Compiler.compileFunctions(value: Value) {
	when (value) {
		is Value.Argument -> Unit
		is Value.Bool -> Unit
		is Value.I32 -> Unit
		is Value.F32 -> Unit
		is Value.Array -> value.items.forEach {
			compileFunctions(it)
		}
		is Value.ArrayAt -> {
			compileFunctions(value.lhs)
			compileFunctions(value.index)
		}
		is Value.Struct -> value.fields.forEach {
			compileFunctions(it.value)
		}
		is Value.StructAt -> compileFunctions(value.lhs)
		is Value.Switch -> {
			compileFunctions(value.lhs)
			value.functions.forEach {
				compileFunction(it)
			}
		}
		is Value.Call -> {
			compileFunction(value.function)
			compileFunctions(value.param)
		}
		is Value.Inc -> compileFunctions(value.lhs)
		is Value.Dec -> compileFunctions(value.lhs)
		is Value.Plus -> {
			compileFunctions(value.lhs)
			compileFunctions(value.rhs)
		}
		is Value.Minus -> {
			compileFunctions(value.lhs)
			compileFunctions(value.rhs)
		}
		is Value.Times -> {
			compileFunctions(value.lhs)
			compileFunctions(value.rhs)
		}
	}
}

fun Compiler.compileFunction(function: Value.Function) {
	compiledFunctions.getOrCompute(function) {
		val jumpAddress = pc
		val paramIndex = dataHole(size(function.param))
		val retAddress = dataHole(4)
		val outputIndex = types.push(function.param) {
			push(Offset.Direct(paramIndex)) {
				clearValueOffsets {
					index(function.body)
				}
			}
		}
		emitRet(retAddress)
		CompiledFunction(
			inputIndex = paramIndex,
			jumpAddress = jumpAddress,
			retAddress = retAddress,
			outputIndex = outputIndex)
	}
}

fun Compiler.emitCall(jumpAddr: Int, retAddr: Int) {
	codeOutputStream.writeOp(x06_callOpcode)
	codeOutputStream.writeInt(jumpAddr)
	codeOutputStream.writeInt(retAddr)
}

fun Compiler.emitRet(addr: Int) {
	codeOutputStream.writeOp(x07_retOpcode)
	codeOutputStream.writeInt(addr)
}
