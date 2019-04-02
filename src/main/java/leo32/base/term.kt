@file:Suppress("unused")

package leo32.base

import leo.base.Empty

object EmptyTerm

data class Term(
	val string: String,
	val apOrNull: Ap?)

val Empty.term
	get() =
		EmptyTerm

fun Term.plus(string: String, rhsOrNull: Term? = null) =
	if (rhsOrNull == null) Term(string, Ap(this, null))
	else Term(string, Ap(this, rhsOrNull))

fun EmptyTerm.plus(string: String, rhsOrNull: Term? = null) =
	if (rhsOrNull == null) Term(string, null)
	else Term(string, Ap(rhsOrNull, null))