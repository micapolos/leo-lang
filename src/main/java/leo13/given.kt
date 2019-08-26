package leo13

import leo.base.ifOrNull
import leo.base.notNullIf
import leo9.Stack
import leo9.stack

object Outside : AsScriptLine() {
	override val asScriptLine get() = "outside" lineTo script()
}

val outside = Outside

data class Given(val previousStack: Stack<Outside>) : AsScriptLine() {
	override fun toString() = super.toString()
	override val asScriptLine get() = previousStack.asScriptLine("given") { asScriptLine }
}

val Stack<Outside>.argument get() = Given(this)
fun argument(vararg previouses: Outside) = stack(*previouses).argument

val Script.givenOrNull
	get() =
		onlyLineOrNull?.givenOrNull

val ScriptLine.givenOrNull: Given?
	get() =
		ifOrNull(name == "given") {
			rhs.rhsGivenOrNull
		}

// TODO: Support "given"
val Script.rhsGivenOrNull: Given?
	get() =
		notNullIf(isEmpty) { argument() }
