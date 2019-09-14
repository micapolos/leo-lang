package leo13.untyped.pattern

import leo.base.fold
import leo.base.notNullOrError
import leo.base.orNullFold
import leo13.script.*
import leo13.script.Script
import leo13.script.ScriptLine
import leo13.untyped.patternName
import leo13.untyped.rhsOrNull
import leo13.untyped.value.Value
import leo13.untyped.value.value
import leo9.*

data class Pattern(val itemStack: Stack<PatternItem>) {
	override fun toString() = bodyScript.toString()
}

val Stack<PatternItem>.pattern get() = pattern(this)

fun pattern(itemStack: Stack<PatternItem>) = Pattern(itemStack)

fun pattern(vararg items: PatternItem) =
	pattern(stack(*items))

fun pattern(choice: Choice, vararg choices: Choice) =
	pattern(item(choice)).fold(choices) { plus(it) }

fun pattern(line: PatternLine, vararg lines: PatternLine): Pattern =
	pattern(item(choice(line))).fold(lines) { plus(it) }

fun pattern(name: String) = pattern(name lineTo pattern())

val Pattern.linkOrNull: PatternLink?
	get() =
		itemStack.linkOrNull?.let { link ->
			link.stack.pattern linkTo link.value
		}

fun Pattern.plus(item: PatternItem) =
	pattern(itemStack.push(item))

fun Pattern.plus(choice: Choice) =
	plus(item(choice))

fun Pattern.plus(line: PatternLine) =
	plus(choice(line))

val Pattern.isEmpty get() = itemStack.isEmpty

fun Pattern.matches(value: Value): Boolean =
	when (itemStack) {
		is EmptyStack -> true
		is LinkStack -> when (script.lineStack) {
			is EmptyStack -> false
			is LinkStack -> itemStack.link.value.matches(value.itemStack.link.value)
				&& pattern(itemStack.link.stack).matches(Script(script.lineStack.link.stack))
		}
	}

fun Pattern.matches(script: Script): Boolean =
	matches(script.value)

fun Pattern.patternLineOrNull(name: String): PatternLine? =
	itemStack.mapFirst { choiceOrNull?.onlyEitherOrNull?.patternLineOrNull(name) }

fun Pattern.replaceLineOrNull(line: PatternLine): Pattern? =
	when (itemStack) {
		is EmptyStack -> null
		is LinkStack -> itemStack
			.link
			.value
			.replaceLineOrNull(line)
			?.let { replaced -> itemStack.link.stack.push(replaced).pattern }
			?: itemStack
				.link
				.stack
				.pattern
				.replaceLineOrNull(line)
				?.plus(itemStack.link.value)
	}

fun Pattern.getOrNull(name: String): Pattern? =
	linkOrNull?.let { link ->
		link
			.item
			.choiceOrNull
			?.onlyEitherOrNull
			?.rhs
			?.patternLineOrNull(name)
			?.let { link.lhs.plus(item(choice(it.either))) }
	}

fun Pattern.setOrNull(line: PatternLine): Pattern? =
	itemStack
		.linkOrNull
		?.let { itemStackLink ->
			itemStackLink
				.stack
				.pattern
				.replaceLineOrNull(line)
				?.let { replacedRhs ->
					pattern(itemStackLink.stack.push(item(choice(line.name eitherTo replacedRhs))))
				}
		}

fun Pattern.setOrNull(pattern: Pattern): Pattern? =
	pattern.lineStackOrNull?.let { lineStack ->
		orNullFold(lineStack.reverse.seq) { setOrNull(it) }
	}

val Script.unsafeBodyPattern: Pattern
	get() =
		lineStack.map { item(unsafeChoice) }.pattern

val Script.unsafePattern: Pattern
	get() =
		onlyLineOrNull
			?.rhsOrNull(patternName)
			.notNullOrError("pattern")
			.unsafeBodyPattern

val Pattern.bodyScript: Script
	get() =
		itemStack.map { scriptLine }.script

val Pattern.scriptLine: ScriptLine
	get() =
		patternName lineTo bodyScript

val Pattern.script: Script
	get() =
		script(patternName lineTo bodyScript)

val patternReader get() = reader(patternName) { unsafeBodyPattern }
val patternWriter get() = writer<Pattern>(patternName) { bodyScript }

val Pattern.lineStackOrNull: Stack<PatternLine>?
	get() =
		itemStack.mapOrNull { choiceOrNull?.patternLineOrNull }

val Pattern.previousOrNull: Pattern?
	get() =
		itemStack.linkOrNull?.stack?.pattern