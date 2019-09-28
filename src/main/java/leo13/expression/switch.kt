package leo13.expression

import leo13.*
import leo13.script.lineTo

data class Switch(val caseStack: Stack<Case>) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine = "match" lineTo caseStack.scripting.script

	fun plus(case: Case) =
		caseStack.push(case).switch
}

val Stack<Case>.switch get() = Switch(this)

fun switch(vararg cases: Case) = stack(*cases).switch
