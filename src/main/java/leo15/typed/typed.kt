package leo15.typed

import leo14.invoke
import leo14.plus
import leo15.lambda.*
import leo15.ofName
import leo15.string
import leo15.type.emptyTerm

data class Typed(val term: Term, val type: Term) {
	override fun toString() = term.script.plus(ofName(type.script)).string
}

infix fun Term.of(type: Term) = Typed(this, type)

val emptyType = emptyTerm
val intType = Integer.TYPE.valueTerm
val textType = String::class.java.valueTerm

val emptyTyped = emptyTerm of emptyType
val Int.typed get() = valueTerm of intType
val String.typed get() = valueTerm of textType

val Term.first: Term get() = invoke(firstTerm)
val Term.second: Term get() = invoke(secondTerm)

fun Typed.append(name: String, rhs: Typed): Typed =
	term.append(rhs.term) of
		type.append(name.valueTerm.append(rhs.type))

fun Typed.apply(name: String, rhs: Typed): Typed =
	append(name, rhs).apply

val Typed.eval: Typed
	get() =
		term.eval of type

val Typed.apply: Typed
	get() =
		scopeApply(this) ?: this
