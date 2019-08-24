package leo13.script

import leo13.asScript
import leo13.lineTo
import leo9.Stack
import leo9.stack

data class Switch(val caseStack: Stack<Case>) {
	override fun toString() = asScriptLine.toString()
	val asScriptLine get() = "switch" lineTo caseStack.asScript { asScriptLine }
}

fun switch(vararg cases: Case) = Switch(stack(*cases))
