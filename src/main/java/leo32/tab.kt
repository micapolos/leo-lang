@file:Suppress("unused")

package leo32

object Tab

val tab = Tab

val Tab.char get() = '\t'

fun Appendable.append(tab: Tab): Appendable = append(tab.char)