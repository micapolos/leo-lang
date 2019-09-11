package leo13.untyped

import leo.base.notNullOrError
import leo13.script.*
import leo13.script.Script
import leo9.*

data class Pattern(val choiceStack: Stack<Choice>) {
	override fun toString() = bodyScript.toString()
}

val Stack<Choice>.pattern get() = pattern(this)

fun pattern(choiceStack: Stack<Choice>) = Pattern(choiceStack)

fun pattern(vararg choices: Choice) =
	pattern(stack(*choices))

fun pattern(line: PatternLine, vararg lines: PatternLine): Pattern =
	pattern(stack(line, *lines).map { choice(name eitherTo rhs) })

fun pattern(name: String) = pattern(name lineTo pattern())

fun Pattern.plus(choice: Choice) =
	pattern(choiceStack.push(choice))

fun Pattern.plus(line: PatternLine) =
	plus(choice(line))

fun Pattern.matches(script: Script): Boolean =
	when (choiceStack) {
		is EmptyStack -> true
		is LinkStack -> when (script.lineStack) {
			is EmptyStack -> false
			is LinkStack -> choiceStack.link.value.matches(script.lineStack.link.value)
				&& pattern(choiceStack.link.stack).matches(Script(script.lineStack.link.stack))
		}
	}

val patternName: String = "pattern"

val Script.unsafeBodyPattern: Pattern
	get() =
		lineStack.map { unsafeChoice }.pattern

val Script.unsafePattern: Pattern
	get() =
		onlyLineOrNull
			?.rhsOrNull(patternName)
			.notNullOrError("pattern")
			.unsafeBodyPattern

val Pattern.bodyScript: Script
	get() =
		choiceStack.map { scriptLine }.script

val Pattern.script: Script
	get() =
		script(patternName lineTo bodyScript)

val patternReader get() = reader(patternName) { unsafeBodyPattern }
val patternWriter get() = writer<Pattern>(patternName) { bodyScript }
