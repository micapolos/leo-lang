package leo14.lib

import leo14.typed.Line
import leo14.typed.compiler.natives.decompile
import leo14.typed.of

abstract class Obj {
	abstract val term: Term
	abstract val typeLine: Line
	val typedLine get() = term of typeLine
	override fun toString() = typedLine.decompile.toString()
}
