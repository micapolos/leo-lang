package leo32.runtime

import leo.base.Empty
import leo.base.fold
import leo.base.orIfNull

data class Scope(
	val valueToTypeDictionary: Dictionary<Term>,
	val typeToDescribeDictionary: Dictionary<Term>,
	val typeToBodyDictionary: Dictionary<Term>)

val Empty.scope get() =
	Scope(dictionary(), dictionary(), dictionary())

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
		}.copy(typeToDescribeDictionary = typeToDescribeDictionary.put(
			termHasTerm.lhs, termHasTerm.lhs.plus(hasSymbol to termHasTerm.rhs)))

fun Scope.define(case: Case): Scope =
	copy(typeToBodyDictionary = typeToBodyDictionary.put(case.key, case.value))

fun Scope.plusValue(field: TermField) =
	copy(valueToTypeDictionary = valueToTypeDictionary.plus(field))

fun Scope.invoke(vararg lines: Line): Term =
	emptyTerm.fold(lines) { invoke(it) }