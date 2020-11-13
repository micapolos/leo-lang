package leo22.term

import leo22.dsl.*

fun X.termApply(rhs: X): X =
	term(application(lhs(this), rhs(rhs)))
