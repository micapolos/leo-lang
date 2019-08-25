package leo13

import leo.base.notNullOrError
import leo9.Stack
import leo9.stack

data class Switch(val caseStack: Stack<Case>) : Scriptable() {
	override fun toString() = super.toString()
	override val asScriptLine = caseStack.asScriptLine("switch")
}

val Stack<Case>.switch: Switch get() = Switch(this)
fun switch(vararg cases: Case): Switch = stack(*cases).switch

val ScriptLine.switchOrNull: Switch?
	get() = asStackOrNull("switch") { caseOrNull }?.switch

val String.unsafeSwitch
	get() =
		unsafeScriptLine
			.switchOrNull
			.notNullOrError("switch")