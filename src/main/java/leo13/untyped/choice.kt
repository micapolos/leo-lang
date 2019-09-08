package leo13.untyped

import leo13.script.*
import leo9.Stack
import leo9.any
import leo9.push
import leo9.stack

const val choiceName = "choice"

val choiceReader: Reader<Choice> =
	stackReader(choiceName, optionReader, ::choice)

val choiceWriter: Writer<Choice> =
	stackWriter(choiceName, optionWriter, Choice::optionStack)

data class Choice(val optionStack: Stack<Option>) {
	override fun toString() = choiceWriter.string(this)
}

fun choice(optionStack: Stack<Option>) = Choice(optionStack)
fun choice(vararg options: Option) = choice(stack(*options))
fun Choice.plus(option: Option) = choice(optionStack.push(option))

fun Choice.matches(script: Script): Boolean =
	script
		.onlyLineOrNull
		?.let { matches(it) }
		?: false

fun Choice.matches(scriptLine: ScriptLine): Boolean =
	optionStack.any { matches(scriptLine) }
