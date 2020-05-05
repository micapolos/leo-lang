package leo16

import leo13.*
import leo14.Literal
import leo14.NumberLiteral
import leo14.StringLiteral
import leo15.*
import leo15.givingName
import leo15.textName

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
data class NativePatternField(val native: Any?) : PatternField()

data class PatternSentence(val word: String, val pattern: Pattern)

val anyPattern: Pattern = AnyPattern
val PatternValue.pattern: Pattern get() = ValuePattern(this)
val PatternSentence.field: PatternField get() = SentencePatternField(this)
val Any?.nativePatternField: PatternField get() = NativePatternField(this)
val Stack<PatternField>.value get() = PatternValue(this)
fun patternValue(vararg fields: PatternField) = stack(*fields).value
fun pattern(vararg fields: PatternField) = patternValue(*fields).pattern
val emptyPattern = pattern()
val String.pattern get() = pattern(invoke(pattern()))
operator fun String.invoke(pattern: Pattern): PatternField = PatternSentence(this, pattern).field

val Pattern.isEmpty get() = this is ValuePattern && value.fieldStack.isEmpty

operator fun Pattern.plus(field: PatternField): Pattern =
	when (this) {
		AnyPattern -> pattern(anyName.invoke(emptyPattern))
		is ValuePattern -> value.plus(field).pattern
	}

operator fun PatternValue.plus(field: PatternField): PatternValue =
	fieldStack.push(field).value

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
			is NativePatternField -> native.nativeField
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
		is DictionaryField -> field == dictionaryName(anyPattern)
		is NativeField -> field == nativeName(anyPattern)
		is ChoiceField -> false // TODO()
	}

fun Sentence.matches(sentence: PatternSentence): Boolean =
	word == sentence.word && value.matches(sentence.pattern)

fun Literal.matches(field: PatternField): Boolean =
	when (this) {
		is StringLiteral -> field == textName(anyPattern)
		is NumberLiteral -> field == numberName(anyPattern)
	}
