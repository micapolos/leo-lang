package leo16.term.chez

import leo.base.appendParenthesized
import leo.base.appendableString
import leo.spaceChar
import leo16.names.*
import leo16.term.AbstractionTerm
import leo16.term.ApplicationTerm
import leo16.term.Term
import leo16.term.ValueTerm
import leo16.term.VariableTerm

val Term<Chez>.string get() = appendableString { it.append(this, 0) }

fun Appendable.append(term: Term<Chez>, depth: Int): Appendable =
	when (term) {
		is ValueTerm ->
			append(term.value, depth)
		is VariableTerm ->
			appendVariable(depth - term.index - 1)
		is AbstractionTerm ->
			appendParenthesized {
				this
					.append(_lambda)
					.append(spaceChar)
					.appendParenthesized { appendVariable(depth) }
					.append(spaceChar)
					.append(term.bodyTerm, depth.inc())
			}
		is ApplicationTerm ->
			appendParenthesized {
				this
					.append(term.lhsTerm, depth)
					.append(spaceChar)
					.append(term.rhsTerm, depth)
			}
	}

fun Appendable.appendVariable(index: Int): Appendable =
	append("v$index")
