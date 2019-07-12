package lambda.indexed

import leo.base.Nat

data class Argument(val nat: Nat) {
	override fun toString() = string
}

fun argument(nat: Nat) = Argument(nat)
val Nat.argument get() = argument(this)