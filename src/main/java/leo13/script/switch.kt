package leo13.script

import leo13.Scriptable
import leo13.asScriptLine
import leo9.Stack
import leo9.stack

data class Switch(val caseStack: Stack<Case>) : Scriptable() {
	override fun toString() = super.toString()
	override val asScriptLine get() = caseStack.asScriptLine("switch")
}

val Stack<Case>.switch get() = Switch(this)
fun switch(vararg cases: Case) = Switch(stack(*cases))

