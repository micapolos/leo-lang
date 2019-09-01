package leo13.value

import leo13.LeoObject
import leo13.asScript
import leo9.Stack
import leo9.push
import leo9.stack

data class Switch(val caseStack: Stack<Case>) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "switch"
	override val scriptableBody get() = caseStack.asScript
}

val Stack<Case>.switch get() = Switch(this)
fun switch(vararg cases: Case) = Switch(stack(*cases))
fun Switch.plus(case: Case) = caseStack.push(case).switch
