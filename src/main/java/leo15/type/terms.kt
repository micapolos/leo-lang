package leo15.type

import leo14.bigDecimal
import leo15.lambda.*

val emptyTerm = idTerm

val String.term get() = valueTerm
val Int.term get() = bigDecimal.valueTerm

fun add(lhs: Term, lhsIsStatic: Boolean, rhs: Term, rhsIsStatic: Boolean): Term =
	if (lhsIsStatic)
		if (rhsIsStatic) emptyTerm
		else rhs
	else
		if (rhsIsStatic) lhs
		else lhs.append(rhs)

fun Term.unplus(lhsIsStatic: Boolean, rhsIsStatic: Boolean): Pair<Term, Term> =
	if (lhsIsStatic)
		if (rhsIsStatic) emptyTerm to emptyTerm
		else emptyTerm to this
	else
		if (rhsIsStatic) this to emptyTerm
		else invoke(firstTerm) to invoke(secondTerm)

fun Term.decompileUnplus(lhsIsStatic: Boolean, rhsIsStatic: Boolean): Pair<Term, Term> =
	if (lhsIsStatic)
		if (rhsIsStatic) emptyTerm to emptyTerm
		else emptyTerm to this
	else
		if (rhsIsStatic) this to emptyTerm
		else unsafeUnpair
