@file:Suppress("unused")

package leo.binary

object Zero

val zero = Zero

val Zero.boolean get() = false
val Zero.int get() = 0
val Zero.char get() = '0'
