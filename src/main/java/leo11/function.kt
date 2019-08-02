package leo11

import leo.base.notNullIf
import leo9.Stack
import leo9.mapFirst
import leo9.push
import leo9.stack

data class Function(val ruleStack: Stack<Rule>)

fun function(ruleStack: Stack<Rule>) = Function(ruleStack)
operator fun Function.plus(rule: Rule) = Function(ruleStack.push(rule))
fun function(vararg rules: Rule) = function(stack(*rules))

fun Function.call(script: Script): Script =
	ruleStack
		.mapFirst {
			notNullIf(pattern.matches(script)) {
				body.apply(script)
			}
		}
		?: script