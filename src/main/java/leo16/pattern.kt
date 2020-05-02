package leo16

import leo13.Stack
import leo13.map
import leo13.stack
import leo13.zipFoldOrNull
import leo14.Literal
import leo14.NumberLiteral
import leo14.StringLiteral
import leo15.*

sealed class Pattern {
	override fun toString() = asField.toString()
}

object AnyPattern : Pattern()

data class ValuePattern(val value: PatternValue) : Pattern() {
	override fun toString() = super.toString()
}

data class PatternValue(val fieldStack: Stack<PatternField>)

sealed class PatternField
data class SentencePatternField(val sentence: PatternSentence) : PatternField()
data class LiteralPatternField(val literal: Literal) : PatternField()

data class PatternSentence(val word: String, val pattern: Pattern)

val anyPattern: Pattern = AnyPattern
val PatternValue.pattern: Pattern get() = ValuePattern(this)
val PatternSentence.field: PatternField get() = SentencePatternField(this)
val Stack<PatternField>.value get() = PatternValue(this)
fun patternValue(vararg fields: PatternField) = stack(*fields).value
fun pattern(vararg fields: PatternField) = patternValue(*fields).pattern
val String.pattern get() = pattern(invoke(pattern()))
operator fun String.invoke(pattern: Pattern): PatternField = PatternSentence(this, pattern).field

val Pattern.asField: Field
	get() =
		patternName.invoke(asValue)

val Pattern.asValue: Value
	get() =
		when (this) {
			AnyPattern -> value(anyName())
			is ValuePattern -> value.asValue
		}

val PatternValue.asField: Field
	get() =
		leo15.structName(asValue)

val PatternValue.asValue: Value
	get() =
		fieldStack.map { asField }.value

val PatternField.asField: Field
	get() =
		when (this) {
			is SentencePatternField -> sentence.asField
			is LiteralPatternField -> literal.field
		}

val PatternSentence.asField: Field
	get() =
		word(pattern.asValue)

fun Value.matches(pattern: Pattern): Boolean =
	when (pattern) {
		AnyPattern -> true
		is ValuePattern -> matches(pattern.value)
	}

fun Value.matches(value: PatternValue): Boolean =
	true
		.zipFoldOrNull(fieldStack, value.fieldStack) { field, patternField ->
			and(field.matches(patternField))
		}
		?: false

fun Field.matches(field: PatternField): Boolean =
	when (this) {
		is SentenceField -> field is SentencePatternField && sentence.matches(field.sentence)
		is FunctionField -> field == givingName(anyPattern)
		is LibraryField -> field == libraryName(anyPattern)
		is LiteralField -> literal.matches(field)
	}

fun Sentence.matches(sentence: PatternSentence): Boolean =
	word == sentence.word && value.matches(sentence.pattern)

fun Literal.matches(field: PatternField): Boolean =
	when (this) {
		is StringLiteral -> field == textName(anyPattern)
		is NumberLiteral -> field == numberName(anyPattern)
	}
