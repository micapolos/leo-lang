package leo13.script

import leo13.Scriptable
import leo13.asScript
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
