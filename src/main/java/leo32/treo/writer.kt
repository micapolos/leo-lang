package leo32.treo

import leo.binary.Bit

typealias Writer = (Bit) -> Unit

val nullWriter: Writer = { Unit }