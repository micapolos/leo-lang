package leo32.runtime

import leo.base.Empty
import leo.base.fold

data class Scope(
	val selfOrNull: Term?,
	val dispatcher: Dispatcher)

val Empty.scope get() =
	Scope(null, dispatcher)

fun Scope.define(termHasTerm: TermHasTerm): Scope =
	copy(dispatcher = dispatcher
		.put(termHasTerm.lhs caseTo termHasTerm.lhs.leafPlus(termHasTerm.rhs)))

fun Scope.define(case: Case): Scope =
	copy(
		dispatcher = dispatcher.put(case))

fun Scope.plusValue(field: TermField) =
	copy(
		dispatcher = dispatcher.invoke(field))

fun Scope.invoke(vararg lines: Line): Term =
	emptyTerm.fold(lines) { invoke(it) }

fun Scope.bindSelf(term: Term?) =
	copy(selfOrNull = term)
