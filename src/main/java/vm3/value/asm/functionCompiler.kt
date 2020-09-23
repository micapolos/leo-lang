package vm3.value.asm

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import leo.base.Effect
import leo.base.effect
import leo.base.nullableBind
import vm3.asm.Op
import vm3.int
import vm3.layout.Layout
import vm3.type.Type
import vm3.value.Value

data class FunctionCompiler(
	val parentOrNull: FunctionCompiler?,
	val dataSize: Int,
	val opList: PersistentList<Op>,
	val valueAddressMap: PersistentMap<Value, Int?>,
	val valueTypeMap: PersistentMap<Value, Type>,
	val typeLayoutMap: PersistentMap<Type, Layout>
)

val emptyFunctionCompiler =
	FunctionCompiler(
		parentOrNull = null,
		dataSize = 0,
		opList = persistentListOf(),
		valueAddressMap = persistentMapOf(),
		valueTypeMap = persistentMapOf(),
		typeLayoutMap = persistentMapOf())

fun FunctionCompiler.addressEffect(value: Value): Effect<FunctionCompiler, Int?> =
	valueAddressMap[value]
		?.let { effect(it) }
		?: compileAddressEffect(value)

fun FunctionCompiler.compileAddressEffect(value: Value): Effect<FunctionCompiler, Int?> =
	when (value) {
		is Value.Argument -> null
		is Value.F32 -> emitSetEffect(value.float.int)
		is Value.Struct -> TODO()
		is Value.StructAt -> TODO()
		is Value.Switch -> TODO()
		is Value.Call ->
			addressEffect(value.param).nullableBind { paramAddress ->
				functionParamAddress(value.function).let { paramAddress ->
					TODO()
				}
			}
		is Value.Plus ->
			when (type(value)) {
				is Type.F32 -> emitOpEffect(value.lhs, value.rhs) { dst, lhs, rhs -> Op.F32Add(dst, lhs, rhs) }
				else -> null
			}
		is Value.Minus ->
			when (type(value)) {
				is Type.F32 -> emitOpEffect(value.lhs, value.rhs) { dst, lhs, rhs -> Op.F32Sub(dst, lhs, rhs) }
				else -> null
			}
		is Value.Times ->
			when (type(value)) {
				is Type.F32 -> emitOpEffect(value.lhs, value.rhs) { dst, lhs, rhs -> Op.F32Mul(dst, lhs, rhs) }
				else -> null
			}
	} ?: error("compile($value)")

fun FunctionCompiler.emit(op: Op): FunctionCompiler =
	copy(opList = opList.add(op))

fun <T> FunctionCompiler.emitHole(size: Int, fn: FunctionCompiler.(Int) -> T): T =
	copy(dataSize = dataSize + size).fn(dataSize)

fun FunctionCompiler.emitOp(lhs: Value, fn: (Int) -> Op): FunctionCompiler =
	emit(fn(address(lhs)))

fun FunctionCompiler.emitOp(lhs: Value, rhs: Value, fn: (Int, Int) -> Op): FunctionCompiler =
	emit(fn(address(lhs), address(rhs)))

fun FunctionCompiler.emitOpEffect(fn: (Int) -> Op): Effect<FunctionCompiler, Int> =
	copy(
		opList = opList.add(fn(dataSize)),
		dataSize = dataSize + 4) effect dataSize

fun FunctionCompiler.emitSetEffect(value: Int): Effect<FunctionCompiler, Int> =
	emitOpEffect { dst -> Op.Set32(dst, value) }

fun FunctionCompiler.emitOpEffect(rhs: Value, fn: (Int, Int) -> Op): Effect<FunctionCompiler, Int?> =
	addressEffect(rhs).nullableBind { srcIndex ->
		emitOpEffect { dst ->
			fn(dst, srcIndex)
		}
	}

fun FunctionCompiler.emitOpEffect(lhs: Value, rhs: Value, fn: (Int, Int, Int) -> Op): Effect<FunctionCompiler, Int?> =
	addressEffect(lhs).nullableBind { lhsIndex ->
		addressEffect(rhs).nullableBind { rhsIndex ->
			emitOpEffect { dst ->
				fn(dst, lhsIndex, rhsIndex)
			}
		}
	}

fun FunctionCompiler.type(value: Value) =
	valueTypeMap[value] ?: error("type($value)")

fun FunctionCompiler.size(value: Value): Int = TODO()
fun FunctionCompiler.functionParamAddress(function: Value.Function): Int = TODO()

fun FunctionCompiler.address(value: Value): Int =
	valueAddressMap[value] ?: error("address($value)")

