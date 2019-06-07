package data

import leo.base.fail
import leo.binary.Bit
import leo.binary.bit0
import leo.binary.bit1
import leo.binary.isZero

sealed class Data

data class PairData(val at0: Data, val at1: Data) : Data()
data class BitData(var bit: Bit) : Data()

fun data(at0: Data, at1: Data): Data = PairData(at0, at1)
fun data(bit: Bit): Data = BitData(bit)
val data0 get() = data(bit0)
val data1 get() = data(bit1)

fun Data.at(bit: Bit) = when (this) {
	is PairData -> if (bit.isZero) at0 else at1
	is BitData -> fail()
}

val Data.at0 get() = at(bit0)
val Data.at1 get() = at(bit1)

var Data.bit: Bit
	get() = when (this) {
		is PairData -> fail()
		is BitData -> bit
	}
	set(bit) = when (this) {
		is PairData -> fail()
		is BitData -> this.bit = bit
	}

fun Data.select(fn: (Bit, Data) -> Data) = when (this) {
	is PairData -> at0.bit.let { bit -> fn(bit, if (bit.isZero) at1.at0 else at1.at1) }
	is BitData -> fail()
}

val Data.clone
	get() = when (this) {
		is PairData -> PairData(at0, at1)
		is BitData -> BitData(bit)
	}

fun Data.set(data: Data) {
	when (this) {
		is PairData -> set(data as PairData)
		is BitData -> set(data as BitData)
	}
}

fun PairData.set(pairData: PairData) {
	at0.set(pairData.at0)
	at1.set(pairData.at1)
}

fun BitData.set(bitData: BitData) {
	bit = bitData.bit
}
