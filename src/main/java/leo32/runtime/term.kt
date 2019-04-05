@file:Suppress("unused")

package leo32.runtime

import leo.base.*
import leo32.Seq32
import leo32.base.*
import leo32.base.List

data class Term(
	val fieldList: List<TermField>,
	val termListDict: Dict<String, List<Term>>,
	val termListOrNull: TermList?,
	val scriptOrNull: Script?) {
	override fun toString() = appendableString { it.append(this) }
}

val Empty.term get() =
	Term(empty.list(), empty.stringDict(), null, null)

val Term.fieldCount get() =
	fieldList.size

val Term.isEmpty get() =
	fieldCount.isZero

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

val Term.fieldSeq get() =
	fieldList.seq

fun Term.plus(field: TermField) =
	Term(
		fieldList.add(field),
		termListDict.update(field.name) {
			(this?:empty.list()).add(field.value)
		},
		when {
				termListOrNull != null -> termListOrNull.plus(field)
				fieldCount.int == 1 -> termListOrNull(fieldList.at(0.i32), field)
				else -> null
		},
		Script(this, field))

fun term(name: String, vararg names: String) =
	term().plus(name, *names)

fun Term.plus(name: String, vararg names: String): Term =
	fold(names.reversed()) { plus(it to term()) }.plus(name to term())

fun term(vararg fields: TermField) =
	empty.term.fold(fields) { plus(it) }

fun Appendable.append(term: Term): Appendable =
	(this to false).fold(term.fieldSeq) {
		(if (second) first.append('.') else first).append(it) to true
	}.first

val Term.seq32: Seq32 get() =
	fieldList.seq.map { seq32 }.flat

val Term.dictKey get() =
	seq32.dictKey

fun <V: Any> Empty.termDict() =
	dict<Term, V> { dictKey }

val List<Term>.theTerm get() =
	empty.term.fold(seq) { plus("the" to it) }

fun Term.map(fn: Term.() -> Term): Term =
	if (scriptOrNull == null) this
	else scriptOrNull.lhs.fn().plus(scriptOrNull.field.map(fn))

val Term.evalGet: Term get()  =
	scriptOrNull?.evalGet?:this

val Term.evalWrap: Term get() =
	scriptOrNull?.evalWrap?:this
