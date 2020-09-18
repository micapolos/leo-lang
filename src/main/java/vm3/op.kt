package vm3

import leo.base.orNullIf
import java.io.InputStream
import java.io.OutputStream

sealed class Op {
	object Exit : Op()
	object Nop : Op()
	object SysCall : Op()

	data class Jump(val addr: Int) : Op()
	data class JumpIf(val cond: Int, val addr: Int) : Op()
	data class Call(val addr: Int, val retAddr: Int) : Op()

	data class SetConst(val dst: Int, val value: Int) : Op()
	data class Set(val dst: Int, val lhs: Int) : Op()
	data class SetOffset(val dst: Int, val lhs: Int, val offset: Int) : Op()
	data class SetOffsetTimes(val dst: Int, val lhs: Int, val offset: Int, val size: Int) : Op()

	data class I32Inc(val dst: Int, val lhs: Int) : Op()
	data class I32Dec(val dst: Int, val lhs: Int) : Op()

	data class I32Plus(val dst: Int, val lhs: Int, val rhs: Int) : Op()
	data class I32Minus(val dst: Int, val lhs: Int, val rhs: Int) : Op()

	data class F32Plus(val dst: Int, val lhs: Int, val rhs: Int) : Op()
	data class F32Minus(val dst: Int, val lhs: Int, val rhs: Int) : Op()
	data class F32Times(val dst: Int, val lhs: Int, val rhs: Int) : Op()
	data class F32Div(val dst: Int, val lhs: Int, val rhs: Int) : Op()

	data class Unknown(val byte: Byte) : Op()
}

val Op.size: Int
	get() =
		1 + argCount * 4

val Op.argCount: Int
	get() =
		when (this) {
			Op.Exit -> 0
			Op.Nop -> 0
			Op.SysCall -> 0
			is Op.Jump -> 1
			is Op.JumpIf -> 2
			is Op.Call -> 2
			is Op.SetConst -> 2
			is Op.Set -> 2
			is Op.SetOffset -> 3
			is Op.SetOffsetTimes -> 4
			is Op.I32Inc -> 2
			is Op.I32Dec -> 2
			is Op.I32Plus -> 3
			is Op.I32Minus -> 3
			is Op.F32Plus -> 3
			is Op.F32Minus -> 3
			is Op.F32Times -> 3
			is Op.F32Div -> 3
			is Op.Unknown -> 0
		}

fun OutputStream.write(op: Op) {
	when (op) {
		Op.Exit -> writeOp(0x00)
		Op.Nop -> writeOp(0x01)
		Op.SysCall -> writeOp(0x03)

		is Op.Jump -> writeOp(0x03, op.addr)
		is Op.JumpIf -> writeOp(0x5, op.cond, op.addr)
		is Op.Call -> writeOp(0x5, op.addr, op.retAddr)

		is Op.SetConst -> writeOp(0x08, op.dst, op.value)
		is Op.Set -> writeOp(0x09, op.dst, op.lhs)
		is Op.SetOffset -> writeOp(0x0A, op.dst, op.lhs, op.offset)
		is Op.SetOffsetTimes -> writeOp(0x0B, op.dst, op.lhs, op.offset, op.size)

		is Op.I32Inc -> writeOp(0x10, op.dst, op.lhs)
		is Op.I32Dec -> writeOp(0x11, op.dst, op.lhs)
		is Op.I32Plus -> writeOp(0x16, op.dst, op.lhs, op.rhs)
		is Op.I32Minus -> writeOp(0x17, op.dst, op.lhs, op.rhs)

		is Op.F32Plus -> writeOp(0x33, op.dst, op.lhs, op.rhs)
		is Op.F32Minus -> writeOp(0x34, op.dst, op.lhs, op.rhs)
		is Op.F32Times -> writeOp(0x35, op.dst, op.lhs, op.rhs)
		is Op.F32Div -> writeOp(0x36, op.dst, op.lhs, op.rhs)

		is Op.Unknown -> writeOp(op.byte.toInt())
	}
}

fun OutputStream.writeOp(op: Int, dst: Int) {
	writeOp(op)
	writeInt(dst)
}

fun OutputStream.writeOp(op: Int, dst: Int, lhs: Int) {
	writeOp(op)
	writeInt(dst)
	writeInt(lhs)
}

fun OutputStream.writeOp(op: Int, dst: Int, lhs: Int, rhs: Int) {
	writeOp(op)
	writeInt(dst)
	writeInt(lhs)
	writeInt(rhs)
}

fun OutputStream.writeOp(op: Int, dst: Int, lhs: Int, rhs: Int, x: Int) {
	writeOp(op)
	writeInt(dst)
	writeInt(lhs)
	writeInt(rhs)
	writeInt(x)
}

fun InputStream.readOpInt(): Int? =
	read().orNullIf { this == -1 }

fun InputStream.readOp(): Op? =
	readOpInt()?.let { op ->
		when (op) {
			0x00 -> Op.Exit
			0x01 -> Op.Nop
			0x02 -> Op.SysCall

			0x03 -> Op.Jump(readInt())
			0x04 -> Op.JumpIf(readInt(), readInt())
			0x05 -> Op.Call(readInt(), readInt())

			0x08 -> Op.SetConst(readInt(), readInt())
			0x09 -> Op.Set(readInt(), readInt())
			0x0A -> Op.SetOffset(readInt(), readInt(), readInt())
			0x0B -> Op.SetOffsetTimes(readInt(), readInt(), readInt(), readInt())

			0x10 -> Op.I32Inc(readInt(), readInt())
			0x11 -> Op.I32Dec(readInt(), readInt())

			0x16 -> Op.I32Plus(readInt(), readInt(), readInt())
			0x17 -> Op.I32Minus(readInt(), readInt(), readInt())

			0x33 -> Op.F32Plus(readInt(), readInt(), readInt())
			0x34 -> Op.F32Minus(readInt(), readInt(), readInt())
			0x35 -> Op.F32Times(readInt(), readInt(), readInt())
			0x36 -> Op.F32Div(readInt(), readInt(), readInt())

			else -> Op.Unknown(op.toByte())
		}
	}
