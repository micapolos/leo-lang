package leo.base

import leo.binary.Zero
import leo.binary.zero

sealed class Nat {
	override fun toString() = int.toString()
}

data class ZeroNat(val zero: Zero) : Nat() {
	override fun toString() = super.toString()
}

data class SuccNat(val succ: NatSucc) : Nat() {
	override fun toString() = super.toString()
}

data class NatSucc(val nat: Nat) {
	override fun toString() = "succ($nat)"
}

fun nat(zero: Zero): Nat = ZeroNat(zero)
fun nat(succ: NatSucc): Nat = SuccNat(succ)
fun incremented(nat: Nat) = NatSucc(nat)

val Nat.zeroOrNull get() = (this as? ZeroNat)?.zero
val Nat.succOrNull get() = (this as? SuccNat)?.succ

val Zero.nat get() = nat(this)
val Nat.inc get() = nat(incremented(this))
val Nat.dec get() = succOrNull?.nat

val Int.nat: Nat
	get() =
		if (this == 0) nat(zero)
		else dec().nat.inc

fun nat(int: Int) = int.nat

val Nat.int: Int
	get() =
		when (this) {
			is ZeroNat -> 0
			is SuccNat -> succ.nat.int + 1
		}

tailrec fun <R> Nat.switch(fnStackOrNull: Stack<() -> R>?): R? =
	if (fnStackOrNull == null) null
	else when (this) {
		is ZeroNat -> fnStackOrNull.head()
		is SuccNat -> succ.nat.switch(fnStackOrNull.tail)
	}

fun <R> Nat.switch(vararg fns: () -> R): R? =
	switch(stackOrNull(*fns))


tailrec fun Nat.plus(nat: Nat): Nat =
	when (nat) {
		is ZeroNat -> this
		is SuccNat -> inc.plus(nat.succ.nat)
	}

tailrec fun Nat.minusOrNull(nat: Nat): Nat? =
	when (nat) {
		is ZeroNat -> this
		is SuccNat -> succOrNull?.nat?.minusOrNull(nat.succ.nat)
	}
