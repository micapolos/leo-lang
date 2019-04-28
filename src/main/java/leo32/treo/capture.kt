@file:Suppress("unused")

package leo32.treo

import leo.base.Seq
import leo.base.seq

data class Capture(val variable: Var)

fun capture(variable: Var) = Capture(variable)

val Capture.charSeq: Seq<Char> get() = seq('_')