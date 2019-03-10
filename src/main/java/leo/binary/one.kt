@file:Suppress("unused")

package leo.binary

object One

val one = One

val One.isOne get() = true
val One.int get() = 1
val One.digitChar get() = '1'