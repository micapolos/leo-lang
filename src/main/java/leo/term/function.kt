package leo.term

import leo.base.Stack
import leo.base.stack
import leo.base.string

data class Function(
	val ruleStack: Stack<Rule>) {
	override fun toString() = ruleStack.string
}

val Stack<Rule>.function: Function
	get() =
		Function(this)

fun function(rule: Rule, vararg rules: Rule): Function =
	stack(rule, *rules).function