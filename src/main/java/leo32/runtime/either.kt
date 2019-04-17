package leo32.runtime

import leo.base.Empty
import leo.base.empty
import leo.base.fold
import leo.base.string
import leo32.base.*
import leo32.base.List

data class Either(
	val fieldList: List<TypeField>,
	val typeTree: Tree<Type?>) {
	override fun toString() = termField.string
}

val Empty.either get() =
	Either(list(), tree())

fun Either.plus(typeField: TypeField) =
	copy(
		fieldList = fieldList.add(typeField),
		typeTree = typeTree.put(typeField.name.bitSeq, typeField.value))

fun either(vararg fields: TypeField) =
	empty.either.fold(fields) { plus(it) }

fun either(string: String) =
	either(typeField(string))

fun Either.at(symbol: Symbol): Type? =
	typeTree.at(symbol.bitSeq)?.valueOrNull

val Either.seq32 get() =
	typeTree.seq32 { seq32 }

val Either.termField get() =
	eitherSymbol to term()
		.fold(fieldList.seq) {
			plus(it.termField)
		}

val Term.either get() =
	empty.either.fold(fieldSeq) {
		plus(it.typeField)
	}

fun Either.match(termField: TermField): Boolean =
	at(termField.name)?.let {
		it.match(termField.value)
	}?:false
