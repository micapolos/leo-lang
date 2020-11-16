package leo21.typed

import leo14.lambda.Term
import leo21.prim.Prim
import leo21.term.plus
import leo21.type.Line
import leo21.type.Type

fun Typed<Term<Prim>, Type>.plus(rhs: Typed<Term<Prim>, Line>): Typed<Term<Prim>, Type> =
	plus(rhs) { plus(it) }
