package lambda.v2

import leo.base.Nat

data class Argument(val nat: Nat) {
	override fun toString() = string(0)
}

fun argument(nat: Nat) = Argument(nat)
