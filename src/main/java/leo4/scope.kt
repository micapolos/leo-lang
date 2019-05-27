package leo4

import leo.base.Empty
import leo32.Dict
import leo32.dict

data class Scope(
	val types: Dict<Term, Term>,
	val functions: Dict<Term, Term.() -> Term>)

fun scope(empty: Empty) =
	Scope(empty.dict(Term::bitSeq), empty.dict(Term::bitSeq))

