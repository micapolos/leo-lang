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
	data class SetIndirect(val dst: Int, val lhs: Int) : Op()
	data class SetSize(val dst: Int, val src: Int, val size: Int) : Op()

	data class I32Inc(val dst: Int, val lhs: Int) : Op()
	data class I32Dec(val dst: Int, val lhs: Int) : Op()

	data class I32Plus(val dst: Int, val lhs: Int, val rhs: Int) : Op()
	data class I32Minus(val dst: Int, val lhs: Int, val rhs: Int) : Op()
	data class I32Times(val dst: Int, val lhs: Int, val rhs: Int) : Op()

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
			is Op.SetIndirect -> 2
			is Op.SetSize -> 3
			is Op.I32Inc -> 2
			is Op.I32Dec -> 2
			is Op.I32Plus -> 3
			is Op.I32Minus -> 3
			is Op.I32Times -> 3
			is Op.F32Plus -> 3
			is Op.F32Minus -> 3
			is Op.F32Times -> 3
			is Op.F32Div -> 3
			is Op.Unknown -> 0
		}

fun OutputStream.write(op: Op) {
	when (op) {
		Op.Exit -> writeOp(x00_returnOpcode)
		Op.Nop -> writeOp(x01_nopOpcode)
		Op.SysCall -> writeOp(x02_syscallOpcode)

		is Op.Jump -> writeOp(x03_jumpOpcode, op.addr)
		is Op.JumpIf -> writeOp(x04_jumpIfOpcode, op.cond, op.addr)
		is Op.Call -> writeOp(x05_callOpcode, op.addr, op.retAddr)

		is Op.SetConst -> writeOp(x08_setConst32Opcode, op.dst, op.value)
		is Op.Set -> writeOp(x09_set32Opcode, op.dst, op.lhs)
		is Op.SetSize -> writeOp(x0B_setSizeOpcode, op.dst, op.src, op.size)

		is Op.I32Inc -> writeOp(x10_i32IncOpcode, op.dst, op.lhs)
		is Op.I32Dec -> writeOp(x11_i32DecOpcode, op.dst, op.lhs)
		is Op.I32Plus -> writeOp(x16_i32PlusOpcode, op.dst, op.lhs, op.rhs)
		is Op.I32Minus -> writeOp(x17_i32MinusOpcode, op.dst, op.lhs, op.rhs)

		is Op.F32Plus -> writeOp(x33_f32PlusOpcode, op.dst, op.lhs, op.rhs)
		is Op.F32Minus -> writeOp(x34_f32MinusOpcode, op.dst, op.lhs, op.rhs)
		is Op.F32Times -> writeOp(x35_f32TimesOpcode, op.dst, op.lhs, op.rhs)
		is Op.F32Div -> writeOp(x36_f32DivOpcode, op.dst, op.lhs, op.rhs)

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
			x00_returnOpcode -> Op.Exit
			x01_nopOpcode -> Op.Nop
			x02_syscallOpcode -> Op.SysCall

			x03_jumpOpcode -> Op.Jump(readInt())
			x04_jumpIfOpcode -> Op.JumpIf(readInt(), readInt())
			x05_callOpcode -> Op.Call(readInt(), readInt())

			x08_setConst32Opcode -> Op.SetConst(readInt(), readInt())
			x09_set32Opcode -> Op.Set(readInt(), readInt())
			x0A_setIndirect32Opcode -> Op.SetIndirect(readInt(), readInt())
			x0B_setSizeOpcode -> Op.SetSize(readInt(), readInt(), readInt())

			x10_i32IncOpcode -> Op.I32Inc(readInt(), readInt())
			x11_i32DecOpcode -> Op.I32Dec(readInt(), readInt())

			x16_i32PlusOpcode -> Op.I32Plus(readInt(), readInt(), readInt())
			x17_i32MinusOpcode -> Op.I32Minus(readInt(), readInt(), readInt())
			x18_i32TimesOpcode -> Op.I32Times(readInt(), readInt(), readInt())

			x33_f32PlusOpcode -> Op.F32Plus(readInt(), readInt(), readInt())
			x34_f32MinusOpcode -> Op.F32Minus(readInt(), readInt(), readInt())
			x35_f32TimesOpcode -> Op.F32Times(readInt(), readInt(), readInt())
			x36_f32DivOpcode -> Op.F32Div(readInt(), readInt(), readInt())

			else -> Op.Unknown(op.toByte())
		}
	}
