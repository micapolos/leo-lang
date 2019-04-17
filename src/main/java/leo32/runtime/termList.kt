package leo32.runtime

import leo.base.notNullIf
import leo32.base.*
import leo32.base.List

data class TermList(
	val key: Symbol,
	val term0: Term,
	val term1: Term,
	val remainingTermList: List<Term>)

fun termList(key: Symbol, term0: Term, term1: Term, vararg terms: Term) =
	TermList(key, term0, term1, list(*terms))

fun termList(key: String, term0: Term, term1: Term, vararg terms: Term) =
	termList(symbol(key), term0, term1, *terms)

fun termListOrNull(field0: TermField, field1: TermField) =
	notNullIf(field0.name == field1.name) {
		termList(field0.name, field0.value, field1.value)
	}

fun TermList.plus(term: Term) =
	copy(remainingTermList = remainingTermList.add(term))

fun TermList.plus(field: TermField): TermList? =
	notNullIf(field.name == key) {
		plus(field.value)
	}

fun TermList.at(index: I32) =
	when (index.int) {
		0 -> term0
		1 -> term1
		else -> remainingTermList.at(index.dec.dec)
	}

val TermList.size32 get() =
	remainingTermList.size.inc.inc

val TermList.size get() =
	size32.int

fun TermList.at(int: Int) =
	at(int.i32)
