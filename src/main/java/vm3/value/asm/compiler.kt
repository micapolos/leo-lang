package vm3.value.asm

import leo.base.notNullOrError
import vm3.layout.Layout
import vm3.type.layout.Layouts
import vm3.type.Type
import vm3.value.type.Types
import vm3.asm.Op
import vm3.asm.compiledBytes
import vm3.dsl.layout.offset
import vm3.dsl.type.i32
import vm3.type.layout.get
import vm3.value.type.get
import vm3.getOrCompute
import vm3.int
import vm3.optimize
import vm3.value.type.push
import vm3.type.size
import vm3.value.Value

data class Compiler(
	var dataSize: Int = 0,
	val ops: MutableList<Op> = mutableListOf(),
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
	compiler.ops.add(Op.Exit)
	return Compiled(
		compiler.ops.compiledBytes.byteArray,
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
			ops.add(Op.CopyIndirect32(index, offset.index))
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
		setConst32(index, directIndex)
	}

fun Compiler.direct(index: Int): Int =
	dataHole(4).also { dst ->
		ops.add(Op.Copy32(dst, index))
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

		is Value.Inc -> when (type(value)) {
			Type.I32 -> ops.add(Op.I32Inc(dst, index(value.lhs)))
			else -> TODO()
		}
		is Value.Dec -> when (type(value)) {
			Type.I32 -> ops.add(Op.I32Dec(dst, index(value.lhs)))
			else -> TODO()
		}

		is Value.Plus ->
			when (type(value)) {
				Type.I32 -> ops.add(Op.I32Add(dst, index(value.lhs), index(value.rhs)))
				Type.F32 -> ops.add(Op.F32Add(dst, index(value.lhs), index(value.rhs)))
				else -> TODO()
			}
		is Value.Minus ->
			when (type(value)) {
				Type.I32 -> ops.add(Op.I32Sub(dst, index(value.lhs), index(value.rhs)))
				Type.F32 -> ops.add(Op.F32Sub(dst, index(value.lhs), index(value.rhs)))
				else -> TODO()
			}
		is Value.Times ->
			when (type(value)) {
				Type.I32 -> ops.add(Op.I32Mul(dst, index(value.lhs), index(value.rhs)))
				Type.F32 -> ops.add(Op.F32Mul(dst, index(value.lhs), index(value.rhs)))
				else -> TODO()
			}
	}
}

fun Compiler.add32(lhs: Int): Offset =
	dataHole(4).let { dst ->
		setConst32(dst, lhs)
		Offset.Direct(dst)
	}

fun Compiler.setConst32(dst: Int, src: Int) {
	ops.add(Op.Set32(dst, src))
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
	ops.add(Op.CopyBlock(dst, src, size))
}

fun Compiler.compileOffset(call: Value.Call): Offset =
	compiledFunctions[call.function]
		.notNullOrError("${call.function} not compiled")
		.let { compiledFunction ->
			set(compiledFunction.inputIndex, call.param)
			ops.add(Op.Call(compiledFunction.jumpAddress, compiledFunction.retAddress))
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
			depointerOffset(addOp(i32, structOffset, fieldOffset) { a, b, c -> Op.I32Add(a, b, c) })
		}
	}

fun Compiler.add(arrayAt: Value.ArrayAt): Offset =
	pointerOffset(arrayAt.lhs).let { arrayOffset ->
		offset(arrayAt.index).let { indexOffset ->
			add32(size(arrayAt)).let { sizeOffset ->
				addOp(i32, indexOffset, sizeOffset) { a, b, c -> Op.I32Mul(a, b, c) }.let { itemOffset ->
					depointerOffset(addOp(i32, arrayOffset, itemOffset) { a, b, c -> Op.I32Add(a, b, c) })
				}
			}
		}
	}

fun Compiler.addOp(type: Type, lhs: Offset, rhs: Offset, fn: (Int, Int, Int) -> Op): Offset =
	index(lhs).let { lhs ->
		index(rhs).let { rhs ->
			dataHole(type.size).let { dst ->
				ops.add(fn(dst, lhs, rhs))
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

val Compiler.pc get() = ops.size

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
		ops.add(Op.Ret(retAddress))
		CompiledFunction(
			inputIndex = paramIndex,
			jumpAddress = jumpAddress,
			retAddress = retAddress,
			outputIndex = outputIndex)
	}
}
