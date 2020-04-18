package leo15.core

import leo14.ScriptLine
import leo14.scriptLine
import leo15.lambda.Term
import leo15.type.emptyTerm
import leo15.typeName

data class Typ<T : Leo<T>>(override val scriptLine: ScriptLine, val fn: Term.() -> T) : Leo<Typ<T>>() {
	override val typ: Typ<Typ<T>> get() = Typ(typeName.scriptLine) { null!! }
	override val term = emptyTerm
}

abstract class Leo<T : Leo<T>> {
	final override fun toString() = scriptLine.string
	abstract val typ: Typ<T>
	abstract val term: Term
	abstract val scriptLine: ScriptLine
}

fun <T : Leo<T>> Typ<T>.leo(term: Term): T =
	term.fn()

fun <T : Leo<T>> Term.leo(typ: Typ<T>): T =
	typ.fn.invoke(this)