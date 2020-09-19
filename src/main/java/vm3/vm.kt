package vm3

import leo.base.println

class Vm(
	val data: ByteArray,
	val code: ByteArray,
	var pc: Int = 0,
	val syscall: () -> Unit = { error("Unknown system call") }
)

val Vm.dump: Unit
	get() =
		disassemble.println

val Vm.disassemble: String
	get() =
		"pc: ${pc.hexString}\n--- code ---\n${code.disassemble}--- data ---\n${data.contentToString()}"

@OptIn(ExperimentalStdlibApi::class)
fun Vm.run() {
	while (true) {
		when (val op = fetchOp()) {
			x00_returnOpcode -> return
			x01_nopOpcode -> Unit
			x02_syscallOpcode -> syscall()

			x03_jumpOpcode -> jump(fetch32())
			x04_jumpIfOpcode -> jumpIf(fetch32(), fetch32())
			x05_jumpTable -> jumpTable(fetch32(), fetch32())
			x06_callOpcode -> call(fetch32(), fetch32())

			x08_setConst32Opcode -> setConstant(fetch32(), fetch32())
			x09_set32Opcode -> copy(fetch32(), fetch32())
			x0A_setIndirect32Opcode -> setIndirect(fetch32(), fetch32())
			x0B_setSizeOpcode -> setSize(fetch32(), fetch32(), fetch32())

			x10_i32IncOpcode -> intOp1(Int::inc)
			x11_i32DecOpcode -> intOp1(Int::dec)
			x12_i32UnaryMinusOpcode -> intOp1(Int::unaryMinus)
			x13_i32CountLeadingZeroBitsOpcode -> intOp1(Int::countLeadingZeroBits)
			x14_i32CountTrailingZeroBitsOpcode -> intOp1(Int::countTrailingZeroBits)
			x15_bitCountOpcode -> intOp1(Integer::bitCount)
			x16_i32PlusOpcode -> intOp2(Int::plus)
			x17_i32MinusOpcode -> intOp2(Int::minus)
			x18_i32TimesOpcode -> intOp2(Int::times)
			x19_i32DivOpcode -> intOp2(Int::div)
			x20_i32ShlOpcode -> intOp2(Int::rem)

			x1B_i32InvOpcode -> intOp1(Int::inv)
			x1D_i32AndOpcode -> intOp2(Int::and)
			x1E_i32OrOpcode -> intOp2(Int::or)
			x1F_i32XorOpcode -> intOp2(Int::xor)
			x20_i32ShlOpcode -> intOp2(Int::shl)
			x21_i32ShrOpcode -> intOp2(Int::shr)
			x22_i32UshrOpcode -> intOp2(Int::ushr)
			x23_i32RolOpcode -> intOp2(Int::rotateLeft)
			x24_i32RorOpcode -> intOp2(Int::rotateRight)

			x28_i32IsZeroOpcode -> intTest1 { this == 0 }
			x29_i32EqOpcode -> intTest2 { this == it }
			x2A_i32NeqOpcode -> intTest2 { this != it }
			x2B_i32LtOpcode -> intTest2 { this < it }
			x2C_i32GtOpcode -> intTest2 { this > it }
			x2D_i32LeOpcode -> intTest2 { this <= it }
			x2E_i32GeOpcode -> intTest2 { this >= it }

			x33_f32PlusOpcode -> floatOp2(Float::plus)
			x34_f32MinusOpcode -> floatOp2(Float::minus)
			x35_f32TimesOpcode -> floatOp2(Float::times)
			x36_f32DivOpcode -> floatOp2(Float::div)
			x37_f32RemOpcode -> floatOp2(Float::rem)

			x40_f32IsZeroOpcode -> floatTest1 { this == 0f }
			x41_f32EqOpcode -> floatTest2 { this == it }
			x42_f32NeqOpcode -> floatTest2 { this != it }
			x43_f32LtOpcode -> floatTest2 { this < it }
			x44_f32GtOpcode -> floatTest2 { this > it }
			x45_f32LeOpcode -> floatTest2 { this <= it }
			x46_f32GeOpcode -> floatTest2 { this >= it }

			else -> error("unknown op: $op")
		}
	}
}

inline fun Vm.codeByte(index: Int) =
	code.byte(index)

inline fun Vm.codeInt(index: Int) =
	code.int(index)

inline fun Vm.codeSet(index: Int, int: Int) =
	code.set(index, int)

inline fun Vm.dataInt(index: Int) =
	data.int(index)

inline fun Vm.dataSet(index: Int, int: Int) =
	data.set(index, int)

inline fun Vm.setConstant(index: Int, value: Int) =
	dataSet(index, value)

inline fun Vm.copy(index: Int, addr: Int) =
	dataSet(index, dataInt(addr))

inline fun Vm.setIndirect(index: Int, addr: Int) =
	dataSet(index, dataInt(dataInt(addr)))

inline fun Vm.setSize(dst: Int, src: Int, size: Int) =
	data.copyInto(
		destination = data,
		destinationOffset = dst,
		startIndex = src,
		endIndex = src + size)

inline fun Vm.addOffset(dst: Int, src: Int, offset: Int) =
	dataSet(dst, dataInt(src) + offset)

inline fun Vm.addIndex(dst: Int, src: Int, index: Int, size: Int) =
	dataSet(dst, dataInt(src) + dataInt(index) * size)

inline fun Vm.advance8() =
	also { pc += 1 }

inline fun Vm.advance32() =
	also { pc += 4 }

inline fun Vm.fetch8() =
	codeByte(pc).also { advance8() }

inline fun Vm.fetch32() =
	codeInt(pc).also { advance32() }

inline fun Vm.fetchOp() =
	fetch8().toInt()

inline fun Vm.fetchIndex() =
	fetch32()

inline fun Vm.jump(index: Int) =
	also { pc = index }

inline fun Vm.jumpIf(cond: Int, index: Int) =
	also { if (dataInt(cond).boolean) jump(index) }

inline fun Vm.jumpTable(size: Int, index: Int) =
	also { pc = codeInt(pc + 4 * dataInt(index)) }

inline fun Vm.call(jumpIndex: Int, retIndex: Int) =
	also {
		codeSet(retIndex, pc)
		jump(jumpIndex)
	}

inline fun Vm.intOp1(fn: Int.() -> Int) =
	dataSet(fetchIndex(), fn(dataInt(fetchIndex())))

inline fun Vm.intOp2(fn: Int.(Int) -> Int) =
	dataSet(fetchIndex(), fn(dataInt(fetchIndex()), dataInt(fetchIndex())))

inline fun Vm.intTest1(fn: Int.() -> Boolean) =
	intOp1 { fn().int }

inline fun Vm.intTest2(fn: Int.(Int) -> Boolean) =
	intOp2 { fn(it).int }

inline fun Vm.floatOp1(fn: Float.() -> Float) =
	intOp1 { float.fn().int }

inline fun Vm.floatOp2(fn: Float.(Float) -> Float) =
	intOp2 { float.fn(it.float).int }

inline fun Vm.floatTest1(fn: Float.() -> Boolean) =
	intTest1 { float.fn() }

inline fun Vm.floatTest2(fn: Float.(Float) -> Boolean) =
	intTest2 { float.fn(it.float) }

inline val Int.float get() = Float.fromBits(this)
inline val Float.int get() = toRawBits()
inline val Int.boolean get() = this == 0
inline val Boolean.int get() = if (this) 0 else -1