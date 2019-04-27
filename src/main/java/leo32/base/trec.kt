package leo32.base

import leo.base.notNullIf
import leo.binary.Bit
import leo.binary.bit0
import leo.binary.bit1

sealed class Trec<V>(
	var trace: Trec<V>? = null)

data class LeafTrec<V>(
	val leaf: Leaf<V>) : Trec<V>()

data class AtBitTrec<V>(
	val bit: Bit,
	val trec: Trec<V>) : Trec<V>()

data class BranchTrec<V>(
	val branch: Branch<Trec<V>>) : Trec<V>()

data class RecTrec<V>(
	val rec: Rec) : Trec<V>()

fun <V> trec(rec: Rec): Trec<V> = RecTrec(rec)
fun <V> trec(leaf: Leaf<V>): Trec<V> = LeafTrec(leaf)
fun <V> trec(at0: At0<Trec<V>>, at1: At1<Trec<V>>): Trec<V> = BranchTrec(branch(at0(), at1()))
fun <V> trec(bit: Bit, trec: Trec<V>): Trec<V> = AtBitTrec(bit, trec)
fun <V> trec(at0: At0<Trec<V>>): Trec<V> = trec(bit0, at0())
fun <V> trec(at1: At1<Trec<V>>): Trec<V> = trec(bit1, at1())

val <V> Trec<V>.leafOrNull get() = (this as LeafTrec<V>).leaf

val <V> Trec<V>.enter0 get() = enter(bit0)
val <V> Trec<V>.enter1 get() = enter(bit1)

fun <V> Trec<V>.withTrace(trec: Trec<V>): Trec<V> {
	if (trace != null) error("cyclic recursion")
	trace = trec
	return this
}

fun <V> Trec<V>.enter(bit: Bit): Trec<V>? =
	when (this) {
		is LeafTrec -> null
		is AtBitTrec -> notNullIf(this.bit == bit) { trec.withTrace(this) }
		is BranchTrec -> branch.at(bit).withTrace(this)
		is RecTrec -> null
	}?.invoke

val <V> Trec<V>.invoke: Trec<V>?
	get() =
		when (this) {
			is LeafTrec -> this
			is AtBitTrec -> this
			is BranchTrec -> this
			is RecTrec -> exit?.exit(rec)
		}

val <V> Trec<V>.exit: Trec<V>?
	get() {
		val exited = trace
		trace = null
		return exited
	}

tailrec fun <V> Trec<V>.exit(rec: Rec): Trec<V>? {
	val backRec = rec.recOrNull
	return if (backRec == null) this
	else trace?.exit.exit(backRec)
}

