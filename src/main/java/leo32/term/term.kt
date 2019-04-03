@file:Suppress("unused")

package leo32.term

import leo.base.*
import leo32.base.*
import leo32.base.List
import leo32.field
import leo32.seq32

data class Term(
	val fieldList: List<TermField>,
	val termListDict: Dict<String, List<Term>>,
	val scriptOrNull: Script?) {
	override fun toString() = appendableString { it.append(this) }
}

val Empty.term get() =
	Term(empty.list(), empty.stringDict(), null)

val Term.fieldCount get() =
	fieldList.size

val Term.isEmpty get() =
	fieldCount.isZero

fun Term.at(index: I32): TermField =
	fieldList.at(index)

fun Term.at(name: String): List<Term> =
	termListDict.at(name)!!

fun Term.countAt(name: String): I32 =
	at(name).size

fun Term.at(name: String, index: I32): Term =
	at(name).at(index)

fun Term.onlyAt(name: String): Term =
	at(name).only

val Term.fieldSeq get() =
	fieldList.seq

fun Term.plus(field: TermField) =
	copy(
		fieldList = fieldList.add(field),
		termListDict = termListDict.update(field.name) {
			(this?:empty.list()).add(field.value)
		},
		scriptOrNull = Script(this, field))

fun term(string: String) =
	empty.term.plus(string)

fun Term.plus(name: String) =
	plus(name fieldTo empty.term)

fun term(vararg fields: TermField) =
	empty.term.fold(fields) { plus(it) }

fun Appendable.append(term: Term): Appendable =
	(this to false).fold(term.fieldSeq) {
		(if (second) first.append('.') else first).append(it) to true
	}.first

val Term.seq32 get() =
	string.seq32

val Term.dictKey get() =
	seq32.dictKey

fun <V: Any> Empty.termDict() =
	dict<Term, V> { dictKey }

val List<Term>.theTerm get() =
	empty.term.fold(seq) { plus("the" fieldTo it) }

fun Term.resolve(fn: Term.() -> Term): Term =
	if (scriptOrNull == null) this
	else scriptOrNull.lhs.fn().plus(scriptOrNull.field.resolve(fn))