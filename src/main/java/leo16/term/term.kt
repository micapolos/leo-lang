package leo16.term

import leo.base.runWith
import leo14.Script
import leo14.invoke
import leo14.literal
import leo14.plus
import leo14.script
import leo14.untyped.dottedColorsParameter
import leo14.untyped.leoString
import leo14.untyped.scriptLine
import leo16.names.*
import leo16.nativeString

sealed class Term<out T> {
	override fun toString() = dottedColorsParameter.runWith(false) { script.leoString }
}

data class ValueTerm<T>(val value: T) : Term<T>() {
	override fun toString() = super.toString()
}

data class VariableTerm<T>(val index: Int) : Term<T>() {
	override fun toString() = super.toString()
}

data class AbstractionTerm<T>(val bodyTerm: Term<T>) : Term<T>() {
	override fun toString() = super.toString()
}

data class ApplicationTerm<T>(val lhsTerm: Term<T>, val rhsTerm: Term<T>) : Term<T>() {
	override fun toString() = super.toString()
}

val <T> T.valueTerm: Term<T> get() = ValueTerm(this)
fun <T> Int.variableTerm(): Term<T> = VariableTerm(this)
val <T> Term<T>.abstractionTerm: Term<T> get() = AbstractionTerm(this)
fun <T> Term<T>.applicationTerm(term: Term<T>): Term<T> = ApplicationTerm(this, term)

val <T> Term<T>.script: Script
	get() =
		when (this) {
			is ValueTerm -> script("«$value»")
			is VariableTerm -> script(_at(index.literal.scriptLine))
			is AbstractionTerm -> script(_lambda(bodyTerm.script))
			is ApplicationTerm -> lhsTerm.script.plus(_apply(rhsTerm.script))
		}
