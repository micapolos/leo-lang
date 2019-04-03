package leo32.term

import leo.base.appendableString
import leo.base.empty
import leo.base.fold
import leo32.base.*
import leo32.base.List

data class Term(
	val fieldList: List<Field>,
	val termListDict: Dict<String, List<Term>>) {
	override fun toString() = appendableString { it.append(this) }
}

val emptyTerm =
	Term(empty.list(), empty.stringDict())

val Term.fieldCount get() =
	fieldList.size

val Term.isEmpty get() =
	fieldCount.isZero

fun Term.at(index: I32): Field =
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

fun Term.plus(field: Field) =
	copy(
		fieldList = fieldList.add(field),
		termListDict = termListDict.update(field.name) {
			(this?:empty.list()).add(field.value)
		})

fun term(string: String) =
	emptyTerm.plus(string)

fun Term.plus(name: String) =
	plus(name fieldTo emptyTerm)

fun term(vararg fields: Field) =
	emptyTerm.fold(fields) { plus(it) }

fun Appendable.append(term: Term): Appendable =
	(this to false).fold(term.fieldSeq) {
		(if (second) first.append('.') else first).append(it) to true
	}.first
