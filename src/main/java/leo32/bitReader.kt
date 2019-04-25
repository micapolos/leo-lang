package leo32

import leo.base.Empty
import leo.base.ifOrNull
import leo.base.runIf
import leo.base.ushr1
import leo.binary.Bit
import leo.binary.isZero
import kotlin.experimental.or

data class BitReader(
	val byteReader: ByteReader,
	val byteAcc: Byte,
	val byteMask: Byte)

val ByteReader.bitReader
	get() =
		BitReader(this, 0.toByte(), 0x80.toByte())

val Empty.bitReader
	get() =
		byteReader.bitReader

fun BitReader.plus(bit: Bit): BitReader? =
	byteAcc
		.runIf(!bit.isZero) { byteAcc.or(byteMask) }
		.let { newByteAcc ->
			byteMask.ushr1.let { newByteMask ->
				if (newByteMask == 0.toByte()) byteReader.plus(newByteAcc)?.bitReader
				else copy(byteAcc = newByteAcc, byteMask = newByteMask)
			}
		}

val BitReader.termOrNull
	get() =
		ifOrNull(byteMask == 0x80.toByte()) {
			byteReader.termOrNull
		}

val Term.bitReader
	get() =
		fieldReader.symbolReader.byteReader.bitReader

