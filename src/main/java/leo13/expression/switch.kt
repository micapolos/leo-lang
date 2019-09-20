package leo13.expression

import leo13.*
import leo13.script.lineTo
import leo13.script.script

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
