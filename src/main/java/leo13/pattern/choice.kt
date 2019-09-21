package leo13.pattern

import leo.base.fold
import leo13.Empty
import leo13.ObjectScripting
import leo13.empty
import leo13.script.lineTo
import leo13.script.script

sealed class Choice : ObjectScripting() {
	override fun toString() = scriptingLine.toString()

	override val scriptingLine
		get() =
			"choice" lineTo when (this) {
				is EmptyChoice -> script()
				is LinkChoice -> link.scriptingLine.rhs
			}
}

data class EmptyChoice(val empty: Empty) : Choice() {
	override fun toString() = super.toString()
}

data class LinkChoice(val link: ChoiceLink) : Choice() {
	override fun toString() = super.toString()
}

fun choice(empty: Empty): Choice = EmptyChoice(empty)
fun choice(link: ChoiceLink): Choice = LinkChoice(link)

// TODO: Rename to plusOrNull and detect duplicates
fun Choice.plus(line: PatternLine) = choice(linkTo(line))

fun choice(vararg lines: PatternLine) = choice(empty).fold(lines) { plus(it) }
fun choice(name: String, vararg names: String) = choice(name lineTo pattern()).fold(names) { plus(it lineTo pattern()) }

fun Choice.contains(choice: Choice): Boolean =
	when (this) {
		is EmptyChoice -> choice is EmptyChoice
		is LinkChoice -> choice is LinkChoice && link.contains(choice.link)
	}

fun Choice.contains(pattern: Pattern): Boolean =
	when (this) {
		is EmptyChoice -> false
		is LinkChoice -> pattern is LinkPattern && link.contains(pattern.link)
	}


fun Choice.contains(line: PatternLine) =
	when (this) {
		is EmptyChoice -> false
		is LinkChoice -> link.contains(line)
	}

tailrec fun Choice.plusReversed(choice: Choice): Choice =
	when (choice) {
		is EmptyChoice -> this
		is LinkChoice -> plus(choice.link.line).plusReversed(choice.link.lhs)
	}