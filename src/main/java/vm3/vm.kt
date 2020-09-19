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
		"pc: ${pc.hexString}\n--- code ---\n${code.disassemble}--- data ---\n${data.contentToString()}".println

@OptIn(ExperimentalStdlibApi::class)
fun Vm.run() {
	while (true) {
		when (val op = fetchOp()) {
			0x00 -> return
			0x01 -> Unit
			0x02 -> syscall()

			0x03 -> jump(fetch32())
			0x04 -> jumpIf(fetch32().boolean, fetch32())
			0x05 -> call(fetch32(), fetch32())

			0x08 -> setConstant(fetch32(), fetch32())
			0x09 -> copy(fetch32(), fetch32())
			0x0A -> addOffset(fetch32(), fetch32(), fetch32())
			0x0B -> addIndex(fetch32(), fetch32(), fetch32(), fetch32())

			0x10 -> intOp1(Int::inc)
			0x11 -> intOp1(Int::dec)
			0x12 -> intOp1(Int::unaryMinus)
			0x13 -> intOp1(Int::countLeadingZeroBits)
			0x14 -> intOp1(Int::countTrailingZeroBits)
			0x15 -> intOp1(Integer::bitCount)
			0x16 -> intOp2(Int::plus)
			0x17 -> intOp2(Int::minus)
			0x18 -> intOp2(Int::times)
			0x19 -> intOp2(Int::div)
			0x1A -> intOp2(Int::rem)

			0x1B -> intOp1(Int::inv)
			0x1D -> intOp2(Int::and)
			0x1E -> intOp2(Int::or)
			0x1F -> intOp2(Int::xor)
			0x20 -> intOp2(Int::shl)
			0x21 -> intOp2(Int::shr)
			0x22 -> intOp2(Int::ushr)
			0x23 -> intOp2(Int::rotateLeft)
			0x24 -> intOp2(Int::rotateRight)

			0x28 -> intTest1 { this == 0 }
			0x29 -> intTest2 { this == it }
			0x2A -> intTest2 { this != it }
			0x2B -> intTest2 { this < it }
			0x2C -> intTest2 { this > it }
			0x2D -> intTest2 { this <= it }
			0x2E -> intTest2 { this >= it }

			0x33 -> floatOp2(Float::plus)
			0x34 -> floatOp2(Float::minus)
			0x35 -> floatOp2(Float::times)
			0x36 -> floatOp2(Float::div)
			0x37 -> floatOp2(Float::rem)

			0x40 -> floatTest1 { this == 0f }
			0x41 -> floatTest2 { this == it }
			0x42 -> floatTest2 { this != it }
			0x43 -> floatTest2 { this < it }
			0x44 -> floatTest2 { this > it }
			0x45 -> floatTest2 { this <= it }
			0x46 -> floatTest2 { this >= it }

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

inline fun Vm.jumpIf(cond: Boolean, index: Int) =
	also { if (cond) jump(index) }

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