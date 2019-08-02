package leo11

import leo.base.Empty

sealed class Args<out T>

data class EmptyArgs<out T>(val empty: Empty) : Args<T>()
data class RhsArgs<out T>(val rhs: Rhs<T>) : Args<T>()
data class PairArgs<out T>(val pair: ArgPair<T>) : Args<T>()

data class Lhs<out T>(val value: T)
data class Rhs<out T>(val value: T)
data class ArgPair<out T>(val lhs: Lhs<T>, val rhs: Rhs<T>)

fun <T> args(empty: Empty): Args<T> = EmptyArgs(empty)
fun <T> args(rhs: Rhs<T>): Args<T> = RhsArgs(rhs)
