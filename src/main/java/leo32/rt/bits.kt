package leo32.rt

import leo.base.Seq
import leo.base.flat
import leo.base.map
import leo.binary.Bit
import leo.binary.bitSeq
import leo.binary.isZero

val Bit.symbol
	get() =
		if (isZero) zeroSymbol
		else oneSymbol

fun Scope.field(bit: Bit): Field =
	bitSymbol to emptyValue.plus(bit.symbol to emptyValue)

fun Scope.value(bit: Bit) =
	emptyValue.plus(field(bit))

val Seq<Byte>.byteBitSeq
	get() =
		map { bitSeq }.flat