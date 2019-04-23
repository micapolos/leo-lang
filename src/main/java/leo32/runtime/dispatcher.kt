package leo32.runtime

import leo.base.*
import leo.binary.Bit
import leo.binary.bitSeq
import leo32.base.*
import leo32.base.Tree

data class Dispatcher(
	val tree: Tree<Term?>)

val Tree<Term?>.dispatcher
	get() =
		Dispatcher(this)

val Empty.dispatcher
	get() =
		tree<Term>().dispatcher

fun Dispatcher.put(case: Case) =
	update(case.key) {
		case.value.leaf.tree.dispatcher
	}

fun Dispatcher.update(term: Term, fn: Dispatcher.() -> Dispatcher): Dispatcher =
	term
		.alternativesTermOrNull
		?.let { alternativesTerm ->
			fold(alternativesTerm.fieldSeq) { alternative ->
				update(alternative, fn)
			}
		}
		?: updateField(term.fieldSeq, fn)

fun Dispatcher.update(field: Field, fn: Dispatcher.() -> Dispatcher): Dispatcher =
	updateBit(field.name.bitSeq) {
		update(field.value) {
			updateBit(0.toByte().bitSeq, fn)
		}
	}

fun Dispatcher.updateField(fieldSeq: Seq<Field>, fn: Dispatcher.() -> Dispatcher): Dispatcher =
	fieldSeq
		.seqNodeOrNull
		?.let { fieldSeqNode ->
			update(fieldSeqNode.first) {
				updateField(fieldSeqNode.remaining, fn)
			}
		}
		?: fn()

fun Dispatcher.updateBit(bitSeq: Seq<Bit>, fn: Dispatcher.() -> Dispatcher): Dispatcher =
	tree.updateWithDefault(bitSeq, { null }) {
		dispatcher.fn().tree
	}.dispatcher

fun Dispatcher.at(term: Term): Term? =
	invoke(term).termOrNull

fun Dispatcher.invoke(term: Term) =
	invokeBit(term.bitSeq)

fun Dispatcher.invoke(field: Field) =
	invokeBit(field.bitSeq)

fun Dispatcher.invokeBit(bitSeq: Seq<Bit>) =
	tree
		.at(bitSeq)
		?.dispatcher
		?: empty.dispatcher

val Dispatcher.termOrNull
	get() =
		tree.valueOrNull
