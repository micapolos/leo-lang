package leo32.runtime

import leo.base.Empty
import leo.base.fold
import leo.base.orIfNull

data class Scope(
	val valueToTypeDictionary: Dictionary<Term>,
	val typeToDescribeDictionary: Dictionary<Term>,
	val typeToBodyDictionary: Dictionary<Term>,
	val typeToValueDictionary: Dictionary<Term>,
	val selfOrNull: Term?,
	val dispatcher: Dispatcher)

val Empty.scope get() =
	Scope(dictionary(), dictionary(), dictionary(), dictionary(), null, dispatcher)

fun Scope.define(termHasTerm: TermHasTerm): Scope =
	termHasTerm.rhs
		.listTermSeqOrNull(eitherSymbol)
		?.let { termSeq ->
			fold(termSeq) { eitherTerm ->
				copy(
					valueToTypeDictionary = valueToTypeDictionary.put(
						termHasTerm.lhs.leafPlus(eitherTerm),
						termHasTerm.lhs))
			}
		}.orIfNull {
			copy(valueToTypeDictionary = valueToTypeDictionary.put(
				termHasTerm.lhs.leafPlus(termHasTerm.rhs),
				termHasTerm.lhs))
		}.copy(
			typeToDescribeDictionary = typeToDescribeDictionary
				.put(termHasTerm.lhs, termHasTerm.lhs.plus(hasSymbol to termHasTerm.rhs)),
			typeToValueDictionary = typeToValueDictionary
				.put(termHasTerm.lhs, termHasTerm.lhs.leafPlus(termHasTerm.rhs)),
			dispatcher = dispatcher
				.put(termHasTerm.lhs caseTo termHasTerm.lhs.leafPlus(termHasTerm.rhs)))

fun Scope.define(case: Case): Scope =
	copy(
		typeToBodyDictionary = typeToBodyDictionary.put(case.key, case.value),
		dispatcher = dispatcher.put(case))

fun Scope.plusValue(field: TermField) =
	copy(
		valueToTypeDictionary = valueToTypeDictionary.plus(field),
		dispatcher = dispatcher.invoke(field))

fun Scope.invoke(vararg lines: Line): Term =
	emptyTerm.fold(lines) { invoke(it) }

fun Scope.bindSelf(term: Term?) =
	copy(selfOrNull = term)
