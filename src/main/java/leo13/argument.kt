package leo13

import leo.base.ifOrNull
import leo.base.notNullIf
import leo9.Stack
import leo9.stack

object Previous {
	override fun toString() = asScriptLine.toString()
	val asScriptLine get() = "previous".scriptLine
}

val previous = Previous

data class Argument(val previousStack: Stack<Previous>) {
	override fun toString() = asScriptLine.toString()
	val asScriptLine get() = previousStack.asScriptLine("argument") { asScriptLine }
}

val Stack<Previous>.argument get() = Argument(this)
fun argument(vararg previouses: Previous) = stack(*previouses).argument

val Script.argumentOrNull
	get() =
		onlyLineOrNull?.argumentOrNull

val ScriptLine.argumentOrNull: Argument?
	get() =
		ifOrNull(name == "given") {
			rhs.rhsArgumentOrNull
		}

// TODO: Support "previous"
val Script.rhsArgumentOrNull: Argument?
	get() =
		notNullIf(isEmpty) { argument() }
