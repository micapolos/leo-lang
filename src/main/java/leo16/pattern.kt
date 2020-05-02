package leo16

import leo13.Stack
import leo13.map
import leo13.stack
import leo13.zipFoldOrNull
import leo15.anyName
import leo15.givingName
import leo15.libraryName
import leo15.patternName

sealed class Pattern {
	override fun toString() = asSentence.toString()
}

object AnyPattern : Pattern()

data class ValuePattern(val value: PatternValue) : Pattern() {
	override fun toString() = super.toString()
}

data class PatternValue(val fieldStack: Stack<PatternField>)

sealed class PatternField
data class SentencePatternField(val sentence: PatternSentence) : PatternField()
object FunctionPatternField : PatternField()
object LibraryPatternField : PatternField()

data class PatternSentence(val word: String, val pattern: Pattern)

val anyPattern: Pattern = AnyPattern
val PatternValue.pattern: Pattern get() = ValuePattern(this)
val PatternSentence.field: PatternField get() = SentencePatternField(this)
val functionPatternField: PatternField = FunctionPatternField
val libraryPatternField: PatternField = LibraryPatternField
val Stack<PatternField>.value get() = PatternValue(this)
fun patternValue(vararg fields: PatternField) = stack(*fields).value
fun pattern(vararg fields: PatternField) = patternValue(*fields).pattern
val String.pattern get() = pattern(invoke(pattern()))
operator fun String.invoke(pattern: Pattern): PatternField = PatternSentence(this, pattern).field

val Pattern.asSentence: Sentence
	get() =
		patternName(asScript)

val Pattern.asScript: Value
	get() =
		when (this) {
			AnyPattern -> value(anyName())
			is ValuePattern -> value.asScript
		}

val PatternValue.asSentence: Sentence
	get() =
		leo15.structName(asScript)

val PatternValue.asScript: Value
	get() =
		fieldStack.map { asSentence.field }.value

val PatternField.asSentence: Sentence
	get() =
		when (this) {
			is SentencePatternField -> sentence.asSentence
			FunctionPatternField -> givingName(anyName())
			LibraryPatternField -> libraryName(anyName())
		}

val PatternSentence.asSentence: Sentence
	get() =
		word(pattern.asScript)

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
		is FunctionField -> field is FunctionPatternField
		is LibraryField -> field is LibraryPatternField
	}

fun Sentence.matches(sentence: PatternSentence): Boolean =
	word == sentence.word && value.matches(sentence.pattern)
