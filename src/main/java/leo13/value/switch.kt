package leo13.value

import leo13.script.Scriptable
import leo13.script.asScript
import leo9.Stack
import leo9.push
import leo9.stack

data class Switch(val caseStack: Stack<Case>) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "switch"
	override val scriptableBody get() = caseStack.asScript
}

val Stack<Case>.switch get() = Switch(this)
fun switch(vararg cases: Case) = Switch(stack(*cases))
fun Switch.plus(case: Case) = caseStack.push(case).switch
