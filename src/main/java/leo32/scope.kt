package leo32

import leo.base.Empty
import leo.base.fold
import leo.binary.zero
import leo32.base.*

data class Scope(
	val selfOrNull: Term?,
	val dispatcher: Dispatcher,
	val quoteDepth: I32,
	val isShortQuoted: Boolean)

val Empty.scope get() =
	Scope(null, dispatcher, zero.i32, false)

fun Scope.define(termHasTerm: TermHasTerm): Scope =
	copy(dispatcher = dispatcher
		.put(termHasTerm.lhs gives termHasTerm.lhs.leafPlus(termHasTerm.rhs)))

fun Scope.define(case: TermGivesTerm): Scope =
	copy(
		dispatcher = dispatcher.put(case))

fun Scope.plusValue(field: Field) =
	copy(
		dispatcher = dispatcher.invoke(field))

fun Scope.invoke(vararg lines: Line): Term =
	emptyTerm.fold(lines) { invoke(it) }

fun Scope.bindSelf(term: Term?) =
	copy(selfOrNull = term)

val Scope.quote
	get() =
		copy(quoteDepth = quoteDepth.inc)

val Scope.unquote
	get() =
		copy(quoteDepth = quoteDepth.dec)

val Scope.shortQuote
	get() =
		copy(isShortQuoted = true)

val Scope.isQuoted
	get() =
		!quoteDepth.isZero || isShortQuoted