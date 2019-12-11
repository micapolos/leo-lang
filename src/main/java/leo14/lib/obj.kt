package leo14.lib

import leo14.typed.Line
import leo14.typed.compiler.natives.decompile
import leo14.typed.of

abstract class Obj(val term: Term) {
	abstract val typeLine: Line
	val typedLine get() = term of typeLine
	final override fun toString() = typedLine.decompile.toString()
	final override fun equals(other: Any?) = other is Obj && term == other.term
	final override fun hashCode() = term.hashCode()
}
