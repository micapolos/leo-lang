package leo13.untyped.pattern

import leo13.ObjectScripting
import leo13.script.*
import leo13.script.Script
import leo13.script.ScriptLine
import leo13.untyped.choiceName
import leo13.untyped.value.ValueLine
import leo9.*

data class Choice(val eitherStack: Stack<Either>) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine get() = scriptLine
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

fun Choice.matches(line: ValueLine): Boolean =
	eitherStack.any { matches(line) }

fun Choice.contains(line: PatternLine): Boolean =
	eitherStack.any { contains(line) }

fun Choice.contains(either: Either): Boolean =
	eitherStack.any { contains(either) }

fun Choice.contains(choice: Choice): Boolean =
	choice
		.onlyEitherOrNull
		?.let { contains(it) }
		?: containsAll(choice)

fun Choice.containsAll(choice: Choice): Boolean =
	zipMapOrNull(eitherStack, choice.eitherStack) { either1, either2 ->
		either1.contains(either2)
	}?.all { this } ?: false

fun Choice.contains(item: PatternItem): Boolean =
	when (item) {
		is ChoicePatternItem -> contains(item.choice)
		is ArrowPatternItem -> false
	}

val Choice.onlyEitherOrNull: Either?
	get() =
		eitherStack.onlyOrNull

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

fun Choice.replaceLineOrNull(line: PatternLine): Choice? =
	eitherStack
		.onlyOrNull
		?.run { replaceLineOrNull(line) }
		?.run { choice(this) }

val Choice.patternLineOrNull
	get() =
		eitherStack.onlyOrNull?.patternLine
