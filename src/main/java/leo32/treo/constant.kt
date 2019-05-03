package leo32.treo

import leo.binary.Bit

data class Constant(val bit: Bit)

fun constant(bit: Bit) = Constant(bit)