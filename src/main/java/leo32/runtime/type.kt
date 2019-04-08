package leo32.runtime

import leo.base.*
import leo32.base.*
import leo32.base.List

data class Type(
	val firstEither: Either,
	val remainingEitherList: List<Either>) {
	override fun toString() = term.string
}

fun type(either: Either, vararg eithers: Either) =
	Type(either, list(*eithers))

fun type(vararg fields: EitherField) =
	type(either(*fields))

fun type(name: String, vararg names: String) =
	type(name to type().fold(names.reversed()) { type(it to this) })

fun Type.plus(either: Either) =
	copy(remainingEitherList = remainingEitherList.add(either))

val Type.eitherSeq get() =
	Seq { firstEither.then(remainingEitherList.seq) }

fun <V: Any> Empty.typeDict() =
	dict<Type, V> { term.dictKey }

// TODO: Quote "either"
val Type.term get(): Term =
	if (remainingEitherList.isEmpty) firstEither.term
  else term("either" to firstEither.term).fold(remainingEitherList.seq) { plus("either" to it.term) }

// TODO: Quote "either"
val Term.parseType: Type
	get() =
	if (termListOrNull != null && termListOrNull.key.string == "either")
		type(termListOrNull.term0.parseEither, termListOrNull.term1.parseEither)
			.fold(termListOrNull.remainingTermList.seq) { plus(it.parseEither) }
	else if (fieldCount.int == 1 && fieldAt(0.i32).name == "either")
		type(fieldAt(0.i32).value.parseEither)
	else type(parseEither)

val Term.rawType: Type
	get() =
		type(rawEither)

val Type.seq32 get() =
	term.seq32
