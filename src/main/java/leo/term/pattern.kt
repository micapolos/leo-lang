package leo.term

import leo.Word
import leo.base.fold
import leo.base.string

sealed class Pattern

data class TermPattern(
	val term: Term<Pattern>) : Pattern() {
	override fun toString() = patternType.string(term)
}

data class SwitchPattern(
	val oneOf: OneOf) : Pattern() {
	override fun toString() = oneOf.string

}

data class RecursionPattern(
	val recursion: Recursion) : Pattern() {
	override fun toString() = recursion.string
}

val patternType: Type<Pattern> =
	Type(Pattern::isSimple)

// === constructors

val Term<Pattern>.pattern: Pattern
	get() =
		TermPattern(this)

val Recursion.pattern: Pattern
	get() =
		RecursionPattern(this)

val OneOf.pattern: Pattern
	get() =
		SwitchPattern(this)

val Word.pattern: Pattern
	get() =
		term<Pattern>().pattern

fun pattern(pattern: Pattern, vararg applications: Application<Pattern>): Pattern =
	pattern.fold(applications) { apply(it).pattern }

fun pattern(application: Application<Pattern>, vararg applications: Application<Pattern>): Pattern =
	pattern(application.term.pattern, *applications)

fun pattern(oneOf: OneOf): Pattern =
	oneOf.pattern

fun pattern(recursion: Recursion): Pattern =
	recursion.pattern

// === util

val Pattern.isSimple: Boolean
	get() =
		when (this) {
			is TermPattern -> term.isSimple
			is SwitchPattern -> oneOf.patternStack.tail == null
			is RecursionPattern -> true
		}

// === matching