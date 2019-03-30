package leo32

import leo.binary.Bit

data class Trace(
	val scope: Scope,
	val byte: Byte)

data class Scope(
	val traceOrNull: Trace?,
	val bitToByte: BitToByte)

fun Scope.trace(byte: Byte) =
	Trace(this, byte)

fun Scope.resolve(bitToByte: BitToByte) =
	if (bitToByte.byteIsReady) copy(traceOrNull = trace(bitToByte.byte), bitToByte = newBitToByte)
	else copy(bitToByte = bitToByte)

fun Scope.invoke(bit: Bit): Scope =
	resolve(bitToByte.invoke(bit))