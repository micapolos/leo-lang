package leo13.expression

import leo.base.updateIfNotNull
import leo13.*
import leo13.script.lineTo
import leo13.script.plus

data class Switch(val caseStack: Stack<Case>, val otherOrNull: ExpressionOther?) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine = "match" lineTo
		caseStack.scripting.script.updateIfNotNull(otherOrNull) { plus(it.scriptingLine) }

	fun plus(case: Case) =
		caseStack.push(case).switch

	fun with(other: ExpressionOther) = Switch(caseStack, other)
}

val Stack<Case>.switch get() = Switch(this, null)

fun switch(vararg cases: Case) = stack(*cases).switch
