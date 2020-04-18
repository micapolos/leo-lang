package leo15.core

import leo14.ScriptLine
import leo14.invoke
import leo14.scriptLine
import leo15.lambda.Term
import leo15.lambda.eval
import leo15.lambda.script
import leo15.leoName
import leo15.string
import leo15.termName
import leo15.type.emptyTerm
import leo15.typeName

class Typ<T : Leo<T>>(val scriptLine: ScriptLine, val fn: Term.() -> T) : Leo<Typ<T>>() {
	override val typ: Typ<Typ<T>> get() = Typ(typeName.scriptLine) { null!! }
	override val term = emptyTerm
	override fun equals(other: Any?) = other is Typ<*> && scriptLine == other.scriptLine
	override fun hashCode() = scriptLine.hashCode()
}

abstract class Leo<T : Leo<T>> {
	final override fun toString() = script.string
	abstract val typ: Typ<T>
	abstract val term: Term
	val script get() = leoName(typeName(typ.scriptLine), termName(term.script))
	val eval: T get() = term.eval of typ
}

fun <T : Leo<T>> Typ<T>.leo(term: Term): T =
	term.fn()

infix fun <T : Leo<T>> Term.of(typ: Typ<T>): T =
	typ.fn.invoke(this)