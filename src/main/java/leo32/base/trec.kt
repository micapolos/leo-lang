package leo32.base

import leo.base.notNullIf
import leo.binary.*

sealed class Trec<V>(
	val trace: TrecTrace<V> = TrecTrace())

data class RecTrec<V>(
	val rec: Rec) : Trec<V>()

data class LeafTrec<V>(
	val leaf: Leaf<V>) : Trec<V>()

data class At0Trec<V>(
	val at0: At0<Trec<V>>) : Trec<V>()

data class At1Trec<V>(
	val at1: At1<Trec<V>>) : Trec<V>()

data class BranchTrec<V>(
	val branch: Branch<Trec<V>>) : Trec<V>()

data class TrecTrace<V>(
	var parentTrecOrNull: Trec<V>? = null,
	var bitOrNull: Bit? = null,
	var setFnOrNull: TrecSetFn<V>? = null)

typealias TrecSetFn<V> = Trec<V>.(Bit, Trec<V>) -> Trec<V>

fun <V> TrecTrace<V>.exitFrom(child: Trec<V>): Trec<V>? {
	val parent = parentTrecOrNull
	val bit = bitOrNull
	val fn = setFnOrNull
	parentTrecOrNull = null
	bitOrNull = null
	setFnOrNull = null
	return if (parent != null && bit != null)
		if (fn != null) parent.fn(bit, child)
		else parent
	else null
}

fun <V> trec(rec: Rec) = RecTrec<V>(rec)
fun <V> trec(leaf: Leaf<V>) = LeafTrec(leaf)
fun <V> trec(at0: At0<Trec<V>>) = At0Trec(at0)
fun <V> trec(at1: At1<Trec<V>>) = At1Trec(at1)
fun <V> trec(at0: At0<Trec<V>>, at1: At1<Trec<V>>) = BranchTrec(branch(at0(), at1()))
fun <V> trec(bit: Bit, trec: Trec<V>) = if (bit.isZero) trec(at0(trec)) else trec(at1(trec))

val <V> Trec<V>.leafOrNull get() = (this as LeafTrec<V>).leaf

val <V> Trec<V>.enter0 get() = enter(bit0)
val <V> Trec<V>.enter1 get() = enter(bit1)

fun <V> Trec<V>.withTrace(bit: Bit, trec: Trec<V>): Trec<V> {
	trace.parentTrecOrNull = trec
	trace.bitOrNull = bit
	return this
}

fun <V> Trec<V>.enter(bit: Bit): Trec<V>? =
	when (this) {
		is At0Trec -> notNullIf(bit.isZero) { at0() }
		is At1Trec -> notNullIf(bit.isOne) { at1() }
		is BranchTrec -> branch.at(bit)
		else -> null
	}?.withTrace(bit, this)

val <V> Trec<V>.exit: Trec<V>?
	get() =
		trace.exitFrom(this)
