package leo13.script

import leo13.AsScriptLine
import leo13.asScriptLine
import leo9.Stack
import leo9.push
import leo9.stack

data class Switch(val caseStack: Stack<Case>) : AsScriptLine() {
	override fun toString() = super.toString()
	override val asScriptLine get() = caseStack.asScriptLine("switch")
}

val Stack<Case>.switch get() = Switch(this)
fun switch(vararg cases: Case) = Switch(stack(*cases))
fun Switch.plus(case: Case) = caseStack.push(case).switch
