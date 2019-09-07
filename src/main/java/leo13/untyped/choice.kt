package leo13.untyped

import leo13.script.script
import leo13.scripter.scriptLine
import leo13.scripter.toString
import leo13.scripter.unsafeValue
import leo9.Stack
import leo9.map
import leo9.push
import leo9.stack

val choiceType = leo13.scripter.Scripter(
	"choice",
	{ optionStack.map { optionType.scriptLine(this) }.script },
	{ choice(lineStack.map { optionType.unsafeValue(this) }) })

data class Choice(val optionStack: Stack<Option>) {
	override fun toString() = choiceType.toString(this)
}

fun choice(optionStack: Stack<Option>) = Choice(optionStack)
fun choice(vararg options: Option) = choice(stack(*options))
fun Choice.plus(option: Option) = choice(optionStack.push(option))
