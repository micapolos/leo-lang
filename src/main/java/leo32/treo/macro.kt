package leo32.treo

import leo.base.flatSeq
import leo.base.seq

data class Macro(val treo: Treo)

fun macro(treo: Treo) = Macro(treo)

val Macro.charSeq get() = flatSeq(seq('#'), treo.trailingCharSeq)