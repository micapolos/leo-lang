package leo19.value

import leo19.expr.term
import leo19.term.Term

val Value.term: Term
	get() =
		expr.term
