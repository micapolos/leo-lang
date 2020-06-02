package leo16.term.chez

import leo.base.appendParenthesized
import leo.base.fold
import leo.base.iterate
import leo.base.iterateIndexed
import leo.spaceChar
import leo14.literalString
import leo16.term.Term
import leo16.term.invoke
import leo16.term.lambda
import leo16.term.value

data class Chez(val code: String, val arity: Int)

fun String.codeChez(arity: Int) = Chez(this, arity)
val String.codeChez get() = codeChez(0)

val String.chez: Chez get() = literalString.codeChez
val Int.chez: Chez get() = toString().codeChez
val Double.chez: Chez get() = toString().codeChez
val Boolean.chez: Chez get() = (if (this) "#t" else "#f").codeChez

val String.value get() = chez.value()
val Int.value get() = chez.value()

val Term<Chez>.stringLength get() = apply("string-length")
fun Term<Chez>.stringPlus(term: Term<Chez>) = apply("string-append", term)
fun Term<Chez>.intPlus(term: Term<Chez>) = apply("fx+", term)
fun Term<Chez>.intLessThan(term: Term<Chez>) = apply("fx<", term)

fun Term<Chez>.apply(name: String, vararg terms: Term<Chez>) =
	if (terms.isEmpty())
		name.codeChez.value().invoke(this)
	else name
		.codeChez(terms.size.inc())
		.value()
		.iterate(terms.size.inc()) { lambda(this) }
		.invoke(this)
		.fold(terms) { invoke(it) }

fun Appendable.append(chez: Chez, depth: Int): Appendable =
	if (chez.arity == 0) append(chez.code)
	else appendParenthesized {
		this
			.append(chez.code)
			.iterateIndexed(chez.arity) {
				value
					.append(spaceChar)
					.appendVariable(depth - chez.arity + index)
			}
	}
