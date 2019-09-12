package leo13.untyped

import leo13.script.*
import leo13.script.Script
import leo13.script.ScriptLine
import leo9.*

data class Choice(val eitherStack: Stack<Either>) {
	override fun toString() = scriptLine.toString()
}

val Stack<Either>.choice get() = Choice(this)
fun choice(eitherStack: Stack<Either>) = Choice(eitherStack)
fun choice(vararg eithers: Either) = choice(stack(*eithers))
fun Choice.plus(either: Either) = choice(eitherStack.push(either))
fun choice(line: PatternLine) = choice(line.name eitherTo line.rhs)

fun Choice.matches(script: Script): Boolean =
	script
		.onlyLineOrNull
		?.let { matches(it) }
		?: false

fun Choice.matches(scriptLine: ScriptLine): Boolean =
	eitherStack.any { matches(scriptLine) }

val ScriptLine.unsafeChoice: Choice
	get() =
		if (name == choiceName)
			rhs.lineStack.map { unsafeEither }.choice
		else choice(unsafeBodyEither)

val Choice.scriptLine: ScriptLine
	get() =
		eitherStack
			.onlyOrNull
			?.run { bodyScriptLine }
			?: choiceName lineTo eitherStack.map { scriptLine }.script

fun Choice.linePatternOrNull(name: String): Pattern? =
	eitherStack
		.onlyOrNull
		?.run { linePatternOrNull(name) }

fun Choice.replaceLineOrNull(line: PatternLine): Choice? =
	eitherStack
		.onlyOrNull
		?.run { replaceLineOrNull(line) }
		?.run { choice(this) }

val Choice.patternLineOrNull
	get() =
		eitherStack.onlyOrNull?.patternLine