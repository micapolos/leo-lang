package leo32.term

import leo.base.appendableString
import leo.base.fold
import leo.base.ifNotNull
import leo.base.nullOf

sealed class Term

sealed class SimpleTerm

data class NameTerm(
	val name: String): SimpleTerm() {
	override fun toString() = appendableString { it.append(this) }
}

data class InvokeTerm(
	val name: String,
	val value: Term): SimpleTerm() {
	override fun toString() = appendableString { it.append(this) }
}

data class StructTerm(
	val firstField: Field,
	val nextStructTerm: StructTerm?): Term() {
	override fun toString() = appendableString { it.append(this) }
}

data class Field(
	val name: String,
	val term: Term) {
	override fun toString() = appendableString { it.appendComplex(this) }
}

fun term(name: String): NameTerm =
	NameTerm(name)

fun term(field: Field, vararg fields: Field): StructTerm =
	StructTerm(field, nullOf<StructTerm>().fold(fields) { StructTerm(it, this) })

infix fun String.fieldTo(term: Term): Field =
	Field(this, term)

fun Term.invoke(name: String): StructTerm =
	StructTerm(name fieldTo this, null)

fun NameTerm.invoke(field: Field): StructTerm =
	term("invoke" fieldTo term("it" fieldTo this, field))

fun StructTerm.invoke(field: Field): StructTerm =
	StructTerm(field, this)

// === appendable

fun Appendable.append(term: Term): Appendable =
	when (term) {
		is NameTerm -> append(term)
		is StructTerm -> append(term)
	}

fun Appendable.append(nameTerm: NameTerm): Appendable =
	append(nameTerm.name)

fun Appendable.append(fieldsTerm: StructTerm): Appendable =
	if (fieldsTerm.nextStructTerm == null) appendSimple(fieldsTerm.firstField)
	else appendComplex(fieldsTerm)

fun Appendable.appendComplex(fieldsTerm: StructTerm): Appendable =
	this.append("(").appendComplexFields(fieldsTerm).append(")")

fun Appendable.appendComplexFields(fieldsTerm: StructTerm): Appendable =
	this
		.appendComplex(fieldsTerm.firstField)
		.ifNotNull(fieldsTerm.nextStructTerm) {
			append(", ").appendComplexFields(it)
		}

fun Appendable.appendSimple(field: Field): Appendable =
	append(field.term).append('.').append(field.name)

fun Appendable.appendComplex(field: Field): Appendable =
	append(field.name).append(": ").append(field.term)
