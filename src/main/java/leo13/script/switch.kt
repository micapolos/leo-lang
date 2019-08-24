package leo13.script

import leo13.Scriptable
import leo13.asScript
import leo13.lineTo
import leo9.Stack
import leo9.stack

data class Switch(val caseStack: Stack<Case>) : Scriptable() {
	override fun toString() = super.toString()
	override val asScriptLine get() = "switch" lineTo caseStack.asScript { asScriptLine }
}

fun switch(vararg cases: Case) = Switch(stack(*cases))
