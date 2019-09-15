package leo13.untyped.expression

import leo13.ObjectScripting
import leo13.script
import leo13.script.lineTo
import leo13.script.script
import leo13.scripting
import leo13.untyped.switchName
import leo9.Stack
import leo9.map
import leo9.push
import leo9.stack

data class Switch(val caseStack: Stack<Case>) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine = switchName lineTo caseStack.scripting.script
}

val Stack<Case>.switch get() = Switch(this)

fun switch(vararg cases: Case) = stack(*cases).switch

val Switch.scriptLine
	get() =
		switchName lineTo caseStack.map { scriptLine }.script

fun Switch.plus(case: Case) =
	caseStack.push(case).switch
