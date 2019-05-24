package leo32.base

import leo.base.Seq
import leo.base.The
import leo.base.flatSeq
import leo.binary.Bit
import leo.binary.isZero

data class Branch<out T>(
	val at0: T,
	val at1: T)

fun <T> branch(at0: T, at1: T) =
	Branch(at0, at1)

val <T> T.fullBranch
	get() =
		branch(this, this)

fun <T> Branch<T>.at(bit: Bit) =
	if (bit.isZero) at0 else at1

fun <T> Branch<T>.put(bit: Bit, value: T) =
	if (bit.isZero) branch(value, at1)
	else branch(at0, value)

fun <T> Branch<T>.update(bit: Bit, fn: T.() -> T) =
	if (bit.isZero) branch(at0.fn(), at1)
	else branch(at0, at1.fn())

fun <T, R> Branch<T>.updateEffect(bit: Bit, fn: T.() -> Effect<T, R>): Effect<Branch<T>, R> =
	if (bit.isZero) at0.fn().let { branch(it.target, at1).effect(it.value) }
	else at1.fn().let { branch(at0, it.target).effect(it.value) }

fun <T : Any> branchOrNull(at0: T?, at1: T?): Branch<T?>? =
	if (at0 == null && at1 == null) null
	else Branch(at0, at1)

fun <T : Any> branchOrNull(bit: Bit, value: T?): Branch<T?>? =
	if (bit.isZero) branchOrNull(value, null)
	else branchOrNull(null as T?, value)

fun <T : Any> Branch<T?>.putOrNull(bit: Bit, value: T?): Branch<T?>? =
	if (bit.isZero) branchOrNull(value, at1)
	else branchOrNull(at0, value)

fun <T> branch(bit: Bit, value: T, otherValue: T) =
	if (bit.isZero) branch(value, otherValue)
	else branch(otherValue, value)

fun <T> branch(link: Link<T>, other: Other<T>) =
	branch(link.bit, link.value, other.value)

fun <T> Branch<T>.eq(branch: Branch<T>, fn: T.(T) -> Boolean): Boolean =
	at0.fn(branch.at0) && at1.fn(branch.at1)

fun <T> Branch<T>.contains(branch: Branch<T>, fn: T.(T) -> Boolean): Boolean =
	at0.fn(branch.at0) && at1.fn(branch.at1)

fun <T> Branch<T>.all(fn: T.() -> Boolean): Boolean =
	at0.fn() && at1.fn()

fun <V> Branch<V>.union(branch: Branch<V>, fn: V.(V) -> The<V>?): Branch<V>? =
	at0.fn(branch.at0)?.let { theUnion0 ->
		at0.fn(branch.at0)?.let { theUnion1 ->
			branch(theUnion0.value, theUnion1.value)
		}
	}

fun <V> Branch<V>.bitSeq(valueBitSeqFn: V.() -> Seq<Bit>): Seq<Bit> =
	flatSeq(at0.valueBitSeqFn(), at1.valueBitSeqFn())