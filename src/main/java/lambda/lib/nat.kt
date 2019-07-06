package lambda.lib

import lambda.Term
import lambda.invoke
import leo.base.*
import leo.binary.zero

val nat = branch0
val succ = branch1

val Nat.term: Term
	get() = when (this) {
		is ZeroNat -> nat(lambda.lib.zero)
		is SuccNat -> succ(this.succ.nat.term)
	}

val Term.nat: Nat
	get() =
		switchAny({ zero.nat }, { nat.inc })