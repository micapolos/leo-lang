@file:Suppress("unused")

package leo32.treo

import leo.base.seq

data class Put(val value: Value)

fun put(value: Value) = Put(value)
fun Put.invoke(scope: Scope) = scope.put(value.bit)
val Put.char get() = 'w'
val Put.charSeq get() = seq(char)