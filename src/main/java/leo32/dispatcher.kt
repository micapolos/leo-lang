package leo32

import leo.base.*
import leo.base.Stack
import leo.binary.Bit
import leo.binary.bitSeq
import leo32.base.*
import leo32.base.Tree

data class Dispatcher(
	var previousOrNull: Dispatcher?,
	val tree: Tree<Term?>)

val Tree<Term?>.dispatcher
	get() =
		Dispatcher(null, this)

val Empty.dispatcher
	get() =
		tree<Term>().dispatcher

fun Dispatcher.put(case: TermGivesTerm) =
	update(case.lhs) {
		case.rhs.toLeaf.tree.dispatcher
	}

fun Dispatcher.update(term: Term, fn: Dispatcher.() -> Dispatcher): Dispatcher =
	null
		?: maybeUpdateAlternatives(term, fn)
		?: updateField(term.fieldSeq, fn)

fun Dispatcher.maybeUpdateAlternatives(term: Term, fn: Dispatcher.() -> Dispatcher): Dispatcher? =
	term
		.eitherFieldsDictOrNull
		?.let { alternativesSymbolDict ->
			lazily { lazyDispatcher: Lazy<Dispatcher> ->
				fold(alternativesSymbolDict.valueSeq) { eitherField ->
					update(term(eitherField)) { lazyDispatcher { fn() } }
				}
			}
		}

fun Dispatcher.update(field: Field, fn: Dispatcher.() -> Dispatcher): Dispatcher =
	updateBit(field.name.bitSeq) {
		update(field.value) {
			updateBit(0.toByte().bitSeq, fn)
		}
	}

fun Dispatcher.updateField(fieldSeq: Seq<Field>, fn: Dispatcher.() -> Dispatcher): Dispatcher =
	fieldSeq
		.nodeOrNull
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

fun Stack<Dispatcher>.dispatcherInvoke(bit: Bit): Stack<Dispatcher>? =
	head.tree.at(bit)?.let { tree -> push(tree.dispatcher) }

fun Stack<Dispatcher>.dispatcherInvokeBit(bitSeq: Seq<Bit>): Stack<Dispatcher>? =
	orNullFold(bitSeq) { bit -> dispatcherInvoke(bit) }

val Dispatcher.termOrNull
	get() =
		tree.valueOrNull
