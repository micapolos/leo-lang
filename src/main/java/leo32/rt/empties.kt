@file:Suppress("unused")

package leo32.rt

import leo.base.Empty
import leo.base.Seq
import leo.base.seq

fun Scope.at(empty: Empty, symbol: Symbol) = null
fun Scope.fieldSeq(empty: Empty): Seq<Field> = empty.seq()
