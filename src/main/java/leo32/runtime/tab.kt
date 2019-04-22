@file:Suppress("unused")

package leo32.runtime

import leo.base.clampedByte

object Tab

val tab = Tab

val Tab.char get() = '\t'
val Tab.byte get() = char.clampedByte

fun Appendable.append(tab: Tab) = append(tab.char)
