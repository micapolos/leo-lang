package leo13

import leo.base.Nat

data class IntArgument(val int: Int)
data class NatArgument(val nat: Nat)

val Int.argument get() = IntArgument(this)
val Nat.argument get() = NatArgument(this)

fun argument(int: Int) = int.argument
fun argument(nat: Nat) = nat.argument