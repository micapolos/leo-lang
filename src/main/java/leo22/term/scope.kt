package leo22.term

import leo22.dsl.*

val emptyScope = scope()

fun X.scopePush(value: X): X =
	append_(value)

fun X.scopeValue(term: X): X =
	term.switch_(
		native { value(it) },
		abstraction { value(function(this, it.term)) },
		application { scopeValue(it.lhs.term).valueApply(scopeValue(it.rhs.term)) },
		variable { at_(it.number.int_) })
