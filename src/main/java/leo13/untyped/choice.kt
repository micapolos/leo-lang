package leo13.untyped

import leo13.script.*
import leo13.script.Script
import leo13.script.ScriptLine
import leo9.*

const val choiceName = "choice"

val choiceReader: Reader<Choice> =
	reader(choiceName) {
		choice(lineStack.map { optionReader.unsafeValue(this) })
	}

val choiceWriter: Writer<Choice> =
	writer(choiceName) {
		optionStack.map { optionWriter.scriptLine(this) }.script
	}


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
