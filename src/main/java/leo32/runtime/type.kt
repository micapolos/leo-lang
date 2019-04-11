package leo32.runtime

import leo.base.*
import leo32.Seq32
import leo32.base.*
import leo32.base.List

data class Type(
	val eithers: List<Either>) {
	override fun toString() = term.string
}

val List<Either>.type get() =
	Type(this)

val Empty.type get() =
	list<Either>().type

fun Type.plus(either: Either) =
	eithers.add(either).type

fun type(vararg eithers: Either) =
	empty.type.fold(eithers) { plus(it) }

fun type(string: String) =
	type(either(string to type()))

val Type.seq32: Seq32 get() =
	eithers.seq.map { seq32 }.flat

val Type.term: Term get() =
	term().fold(eithers.seq) {
		plus(it.termField)
	}

val Term.type: Type get() =
	listTermSeqOrNull("either")
		?.let { eitherTermSeq ->
			empty.type.fold(eitherTermSeq) {
				plus(it.either)
			}
		}
		?: empty.type.fold(fieldSeq) {
			plus(term(it).either)
		}

fun Type.invoke(termField: TermField): Type? =
	ifOrNull(!eithers.isEmpty) {
		notNullIf(eithers.first.match(termField)) {
			eithers.dropFirst.type
		}
	}

fun Type.invoke(term: Term): Type? =
	orNull.fold(term.fieldSeq) { this?.invoke(it) }

fun Type.match(term: Term): Boolean =
	invoke(term) == type()