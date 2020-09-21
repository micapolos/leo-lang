package vm3.asm

import leo.base.println
import vm3.Bytes
import vm3.bytes
import vm3.updateInt
import vm3.writeInt
import vm3.writeOp
import vm3.x00_exitOpcode
import vm3.x01_nopOpcode
import vm3.x02_syscallOpcode
import vm3.x03_jumpOpcode
import vm3.x06_callOpcode
import vm3.x07_retOpcode
import vm3.x08_setConst32Opcode
import vm3.x09_set32Opcode
import vm3.x0B_setSizeOpcode
import vm3.x16_i32PlusOpcode
import vm3.x17_i32MinusOpcode
import vm3.x18_i32TimesOpcode
import vm3.x33_f32PlusOpcode
import vm3.x34_f32MinusOpcode
import vm3.x35_f32TimesOpcode
import java.io.ByteArrayOutputStream

data class Compiler(
	val byteArrayOutputStream: ByteArrayOutputStream,
	val opIndexList: MutableList<Int>,
	val addressIndexList: MutableList<Int>
)

val Iterable<Op>.compiledBytes: Bytes
	get() {
		val compiler = Compiler(ByteArrayOutputStream(), mutableListOf(), mutableListOf())
		forEach { op -> compiler.plus(op) }
		val byteArray = compiler.byteArrayOutputStream.toByteArray()
		compiler.addressIndexList.forEach { addressIndex ->
			byteArray.updateInt(addressIndex) { opIndex ->
				compiler.println
				compiler.opIndexList[opIndex]
			}
		}
		return byteArray.bytes
	}

operator fun Compiler.plus(op: Op) {
	opIndexList.add(byteArrayOutputStream.size())
	when (op) {
		Op.Exit -> writeOp(x00_exitOpcode)
		Op.Nop -> writeOp(x01_nopOpcode)
		Op.SysCall -> writeOp(x02_syscallOpcode)
		is Op.Jump -> {
			writeOp(x03_jumpOpcode)
			writeAddress(op.index)
		}
		is Op.Call -> {
			writeOp(x06_callOpcode)
			writeAddress(op.jumpIndex)
			writeInt(op.retIndex)
		}
		is Op.Ret -> writeOp(x07_retOpcode, op.index)
		is Op.Set32 -> writeOp(x08_setConst32Opcode, op.dst, op.src)
		is Op.Copy32 -> writeOp(x09_set32Opcode, op.dst, op.src)
		is Op.CopyBlock -> writeOp(x0B_setSizeOpcode, op.dst, op.src, op.size)
		is Op.I32Add -> writeOp(x16_i32PlusOpcode, op.dst, op.lhs, op.rhs)
		is Op.I32Sub -> writeOp(x17_i32MinusOpcode, op.dst, op.lhs, op.rhs)
		is Op.I32Mul -> writeOp(x18_i32TimesOpcode, op.dst, op.lhs, op.rhs)
		is Op.F32Add -> writeOp(x33_f32PlusOpcode, op.dst, op.lhs, op.rhs)
		is Op.F32Sub -> writeOp(x34_f32MinusOpcode, op.dst, op.lhs, op.rhs)
		is Op.F32Mul -> writeOp(x35_f32TimesOpcode, op.dst, op.lhs, op.rhs)
	}
}

fun Compiler.writeAddress(int: Int) {
	addressIndexList.add(byteArrayOutputStream.size())
	writeInt(int)
}

fun Compiler.writeInt(int: Int) {
	byteArrayOutputStream.writeInt(int)
}

fun Compiler.writeOp(int: Int, vararg ints: Int) {
	byteArrayOutputStream.writeOp(int, *ints)
}
