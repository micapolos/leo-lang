@file:Suppress("unused")

package leo32.runtime

import leo.base.*
import leo32.Seq32
import leo32.base.*
import leo32.base.List

data class Term(
	val globalScope: Scope,
	val localScope: Scope,
	val fieldList: List<TermField>,
	val termListDict: Dict<String, List<Term>>,
	val termListOrNull: TermList?,
	val isList: Boolean,
	val nodeOrNull: Node?,
	val intOrNull: Int?) {
	override fun toString() = appendableString { it.append(this) }
}

val Scope.emptyTerm get() =
	Term(this, this, list(), empty.stringDict(), null, true, null, null)

val Empty.term get() =
	Term(scope, scope, list(), stringDict(), null, true, null, null)

val Term.fieldCount get() =
	fieldList.size

val Term.isEmpty get() =
	fieldCount.isZero

val Term.simpleNameOrNull get() =
	nodeOrNull?.simpleNameOrNull

fun Term.fieldAt(index: I32): TermField =
	fieldList.at(index)

fun Term.at(name: String): List<Term> =
	termListDict.at(name) ?: empty.list()

fun Term.countAt(name: String): I32 =
	at(name).size

fun Term.at(name: String, index: I32): Term =
	at(name).at(index)

fun Term.onlyAt(name: String): Term =
	at(name).only

fun Term.onlyOrNullAt(name: String): Term? =
	at(name).onlyOrNull

fun Term.simpleAtOrNull(name: String): Term? =
	nodeOrNull?.simpleAtOrNull(name)

val Term.fieldSeq get() =
	fieldList.seq

fun Term.invoke(term: Term): Term =
	begin.fold(term.fieldSeq) { invoke(it) }

fun Term.invoke(field: TermField): Term =
	begin
		.invoke(field.value)
		.let { childTerm ->
			(field.name to childTerm).let { resolvedField ->
				localScope
					.plus(resolvedField)
					.let { newLocalScope ->
						copy(localScope = newLocalScope)
							.plus(resolvedField)
							.let { resolvedTerm -> newLocalScope.invoke(resolvedTerm) }
					}
			}
		}

fun Term.invoke(scope: Scope) =
	term()
		.copy(globalScope = scope, localScope = scope)
		.invoke(this)

val Term.begin get() =
	Term(
		globalScope = globalScope,
		localScope = empty.scope,
		fieldList = empty.list(),
		termListDict = empty.stringDict(),
		termListOrNull = null,
		isList = true,
		nodeOrNull = null,
		intOrNull = null)

fun Term.plus(field: TermField) =
	Term(
		globalScope = globalScope,
		localScope = localScope,
		fieldList = fieldList.add(field),
		termListDict = termListDict.update(field.name) {
			(this?:empty.list()).add(field.value)
		},
		termListOrNull = when {
				termListOrNull != null -> termListOrNull.plus(field)
				fieldCount.int == 1 -> termListOrNull(fieldList.at(0.i32), field)
				else -> null
		},
		isList = nodeOrNull == null || nodeOrNull.field.name == field.name,
		nodeOrNull = Node(this, field),
		intOrNull = if (nodeOrNull == null) field.intOrNull else null)

fun Term.plus(term: Term) =
	fold(term.fieldSeq) { plus(it) }

fun term(name: String, vararg names: String) =
	term().plus(name, *names)

fun Term.plus(name: String, vararg names: String): Term =
	fold(names.reversed()) { plus(it to term()) }.plus(name to term())

fun Term.leafPlus(term: Term): Term =
	nodeOrNull?.leafPlus(term)?.term ?: plus(term)

fun term(vararg fields: TermField) =
	empty.term.fold(fields) { plus(it) }

fun invoke(vararg fields: TermField) =
	empty.term.fold(fields) { invoke(it) }

fun Appendable.append(term: Term): Appendable =
	tryAppend { appendSimple(term) } ?: appendComplex(term)

fun Appendable.appendComplex(term: Term): Appendable =
	(this to false).fold(term.fieldSeq) {
			(if (second) first.append(", ") else first).append(it) to true
		}.first

fun Appendable.appendSimple(term: Term): Appendable? =
	when {
			term.nodeOrNull == null -> this
			term.nodeOrNull.lhs.isEmpty -> append(term.nodeOrNull.field)
			else -> null
	}

val Term.seq32: Seq32 get() =
	fieldList.seq.map { seq32 }.flat

val Term.dictKey get() =
	seq32.dictKey

fun <V: Any> Empty.termDict() =
	dict<Term, V> { dictKey }

val List<Term>.theTerm get() =
	empty.term.fold(seq) { plus("the" to it) }

fun Term.map(fn: Term.() -> Term): Term =
	if (nodeOrNull == null) this
	else nodeOrNull.lhs.fn().plus(nodeOrNull.field.map(fn))

val Term.evalQuote: Term? get() =
	nodeOrNull?.evalQuote

val Term.evalGet: Term? get()  =
	nodeOrNull?.evalGet

val Term.evalWrap: Term? get() =
	nodeOrNull?.evalWrap

val Term.evalEquals: Term? get() =
	nodeOrNull?.evalEquals

val Term.evalIs: Term? get() =
	nodeOrNull?.evalIs

val Term.evalMacros: Term get() =
	null
		?:evalQuote
		?:evalGet
		?:evalWrap
		?:evalEquals
		?:evalIs
		?:this

val Script.term get() =
	term().fold(lineSeq) { plus(it.field) }

val Term.script: Script get() =
	Script(list<Line>().fold(fieldSeq) { add(it.line) })

fun invoke(vararg lines: Line): Script =
	empty.term.fold(lines) { invoke(it.field) }.script

val Term.clear get() =
	globalScope.emptyTerm

fun term(boolean: Boolean) =
	term(termField(boolean))

fun term(int: Int) =
	term(termField(int))

// TODO: Re-define reverse function!!!
fun Term.defineIs(term: Term) =
	copy(globalScope = globalScope.define(this to function(term.leafPlus(this))))