package leo22.typed

import leo.base.fold
import leo22.dsl.*
import leo22.term.nilTerm
import leo22.term.termPlus
import leo22.type.lineIsStatic
import leo22.type.nilType
import leo22.type.structIsStatic

val nilTyped = typed(nilTerm, nilType)
fun typed(vararg lines: X): X = nilTyped.fold(lines) { typedPlus(it) }
fun String.typedLineTo(rhs: X) = typed(rhs.term, rhs.type)

fun X.typedPlus(rhs: X): X =
	typed(
		if (structIsStatic)
			if (rhs.lineIsStatic) nilTerm
			else rhs.term
		else
			if (rhs.lineIsStatic) lhs.term
			else lhs.term.termPlus(rhs.term),
		type.struct.append_(rhs).asType)

