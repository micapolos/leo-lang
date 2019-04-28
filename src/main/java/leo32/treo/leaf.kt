@file:Suppress("unused")

package leo32.treo

import leo.base.seq

object Leaf

val leaf = Leaf

val Leaf.charSeq get() = seq<Char>()