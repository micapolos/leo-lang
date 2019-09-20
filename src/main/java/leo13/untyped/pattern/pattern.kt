package leo13.untyped.pattern

import leo.base.fold
import leo.base.ifOrNull
import leo.base.notNullOrError
import leo.base.orNullFold
import leo13.*
import leo13.script.*
import leo13.untyped.value.Value
import leo13.untyped.value.value

data class Pattern(val itemStack: Stack<PatternItem>) : ObjectScripting() {
	override fun toString() = bodyScript.toString()
	override val scriptingLine get() = scriptLine
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

fun Pattern.contains(pattern: Pattern): Boolean =
	zipMapOrNull(itemStack, pattern.itemStack) { item1, item2 ->
		item1.contains(item2)
	}?.all { this } ?: false

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

fun Pattern.setOrNull(newLine: PatternLine): Pattern? =
	itemStack
		.linkOrNull
		?.let { itemStackLink ->
			itemStackLink
				.value
				.lineOrNull
				?.let { line ->
					line
						.rhs
						.replaceLineOrNull(newLine)
						?.let { pattern(itemStackLink.stack.push(item(choice(line.name eitherTo it)))) }
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

val Pattern.contentOrNull: Pattern?
	get() =
		linkOrNull?.item?.choiceOrNull?.onlyEitherOrNull?.rhs

val Pattern.staticScriptOrNull: Script?
	get() =
		itemStack.mapOrNull { staticScriptLineOrNull }?.script

fun pattern(script: Script): Pattern =
	pattern(script.lineStack.map { item(choice(patternLine(this))) })

fun Pattern.leafPlusOrNull(pattern: Pattern): Pattern? =
	linkOrNull.let { link ->
		if (link != null) link.leafPlusOrNull(pattern)?.pattern
		else pattern
	}

val Pattern.onlyNameOrNull: String?
	get() =
		linkOrNull?.run {
			ifOrNull(lhs.isEmpty) {
				item.lineOrNull?.run {
					ifOrNull(rhs.isEmpty) {
						name
					}
				}
			}
		}