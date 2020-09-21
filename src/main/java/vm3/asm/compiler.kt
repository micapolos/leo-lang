package vm3.asm

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import leo.base.byte0
import leo.base.byte1
import leo.base.byte2
import leo.base.byte3
import vm3.Bytes
import vm3.bytes
import vm3.updateLittleEndian
import vm3.x00_exitOpcode
import vm3.x01_nopOpcode
import vm3.x02_syscallOpcode
import vm3.x03_jumpOpcode
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

data class Compiler(
	val emittedByteList: PersistentList<Byte>,
	val opIndexList: PersistentList<Int>,
	val addressIndexList: PersistentList<Int>
)

val emptyCompiler = Compiler(persistentListOf(), persistentListOf(), persistentListOf())

val Compiler.bytes: Bytes
	get() =
		addressIndexList.fold(emittedByteList) { byteList, addressIndex ->
			byteList.updateLittleEndian(addressIndex) { opIndex ->
				opIndexList[opIndex]
			}
		}.toByteArray().bytes

fun Compiler.emit(op: Op): Compiler =
	markOp.run {
		when (op) {
			Op.Exit -> emitOp(x00_exitOpcode)
			Op.Nop -> emitOp(x01_nopOpcode)
			Op.SysCall -> emitOp(x02_syscallOpcode)
			is Op.Jump -> emitOp(x03_jumpOpcode).emitAddress(op.index)
			is Op.Call -> emitOp(x06_callOpcode).emitAddress(op.jumpIndex).emit(op.retIndex)
			is Op.Ret -> emitOp(x07_retOpcode, op.index)
			is Op.Set32 -> emitOp(x08_setConst32Opcode, op.dst, op.src)
			is Op.Copy32 -> emitOp(x09_set32Opcode, op.dst, op.src)
			is Op.CopyIndirect32 -> emitOp(x0A_setIndirect32Opcode, op.dst, op.src)
			is Op.CopyBlock -> emitOp(x0B_setSizeOpcode, op.dst, op.src, op.size)
			is Op.I32Inc -> emitOp(x10_i32IncOpcode, op.dst, op.src)
			is Op.I32Dec -> emitOp(x11_i32DecOpcode, op.dst, op.src)
			is Op.I32Add -> emitOp(x16_i32PlusOpcode, op.dst, op.lhs, op.rhs)
			is Op.I32Sub -> emitOp(x17_i32MinusOpcode, op.dst, op.lhs, op.rhs)
			is Op.I32Mul -> emitOp(x18_i32TimesOpcode, op.dst, op.lhs, op.rhs)
			is Op.F32Add -> emitOp(x33_f32PlusOpcode, op.dst, op.lhs, op.rhs)
			is Op.F32Sub -> emitOp(x34_f32MinusOpcode, op.dst, op.lhs, op.rhs)
			is Op.F32Mul -> emitOp(x35_f32TimesOpcode, op.dst, op.lhs, op.rhs)
		}
	}

val Compiler.markOp
	get() =
		copy(opIndexList = opIndexList.add(emittedByteList.size))

val Compiler.markAddress: Compiler
	get() =
		copy(addressIndexList = addressIndexList.add(emittedByteList.size))

fun Compiler.emit(byte: Byte): Compiler =
	copy(emittedByteList = emittedByteList.add(byte))

fun Compiler.emit(int: Int): Compiler =
	this
		.emit(int.byte0)
		.emit(int.byte1)
		.emit(int.byte2)
		.emit(int.byte3)

fun Compiler.emitOp(op: Int, vararg ints: Int): Compiler =
	emit(op.toByte()).run {
		ints.fold(this, Compiler::emit)
	}

fun Compiler.emitAddress(int: Int): Compiler =
	markAddress.emit(int)
