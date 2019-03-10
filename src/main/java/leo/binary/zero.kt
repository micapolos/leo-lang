@file:Suppress("unused")

package leo.binary

object Zero

val zero = Zero

val Zero.isOne get() = false
val Zero.int get() = 0
val Zero.digitChar get() = '0'
