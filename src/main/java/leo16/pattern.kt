package leo16

import leo.base.ifNotNull
import leo.base.orIfNull
import leo.base.orNull
import leo13.Stack
import leo13.isEmpty
import leo13.map
import leo13.push
import leo13.pushAll
import leo13.reverse
import leo13.stack
import leo13.zipFold
import leo14.Literal
import leo14.NumberLiteral
import leo14.StringLiteral
import leo16.names.*

data class Pattern(val isAny: Boolean, val value: PatternValue) {
	override fun toString() = super.toString()
}

data class PatternValue(val fieldStack: Stack<PatternField>)

sealed class PatternField
data class SentencePatternField(val sentence: PatternSentence) : PatternField()
data class NativePatternField(val native: Any?) : PatternField()
data class FunctionPatternField(val function: PatternFunction) : PatternField()

data class PatternSentence(val word: String, val pattern: Pattern)
data class PatternFunction(val pattern: Pattern)

data class PatternMatch(val anyFieldStackOrNull: Stack<Field>?, val fieldStack: Stack<Field>)

val emptyPatternValue: PatternValue = PatternValue(stack())
val emptyPattern = pattern()
val anyPattern: Pattern = Pattern(isAny = true, value = emptyPatternValue)
val PatternValue.pattern: Pattern get() = Pattern(false, this)
val PatternSentence.field: PatternField get() = SentencePatternField(this)
val Pattern.function: PatternFunction get() = PatternFunction(this)
val PatternFunction.field: PatternField get() = FunctionPatternField(this)
val Any?.nativePatternField: PatternField get() = NativePatternField(this)
val Stack<PatternField>.value get() = PatternValue(this)
fun patternValue(vararg fields: PatternField) = stack(*fields).value
fun pattern(vararg fields: PatternField) = patternValue(*fields).pattern
val String.pattern get() = pattern(invoke(pattern()))
operator fun String.invoke(pattern: Pattern): PatternField = PatternSentence(this, pattern).field

val Pattern.isEmpty get() = !isAny && value.fieldStack.isEmpty

operator fun Pattern.plus(field: PatternField): Pattern =
	copy(value = value.plus(field))

operator fun PatternValue.plus(field: PatternField): PatternValue =
	fieldStack.push(field).value

val Pattern.asField: Field
	get() =
		_pattern.invoke(asValue)

val Pattern.anyValue: Value
	get() =
		if (isAny) value(_any()) else value()

val Pattern.asValue: Value
	get() =
		anyValue.plus(value.asValue)

val PatternValue.asField: Field
	get() =
		_struct(asValue)

val PatternValue.asValue: Value
	get() =
		fieldStack.map { asField }.value

val PatternField.asField: Field
	get() =
		when (this) {
			is SentencePatternField -> sentence.asField
			is NativePatternField -> native.nativeField
			is FunctionPatternField -> function.asField
		}

val PatternSentence.asField: Field
	get() =
		word(pattern.asValue)

val PatternFunction.asField: Field
	get() =
		_function(pattern.asValue)

val emptyMatch = PatternMatch(null, stack())

fun PatternMatch.plus(field: Field) =
	copy(fieldStack = fieldStack.push(field))

fun PatternMatch.anyPlus(field: Field) =
	copy(anyFieldStackOrNull = anyFieldStackOrNull.orIfNull { stack() }.push(field))

val Value.match get() = PatternMatch(null, fieldStack.reverse)

val PatternMatch.value
	get() =
		stack<Field>()
			.ifNotNull(anyFieldStackOrNull) { pushAll(it) }
			.pushAll(fieldStack)
			.reverse
			.value

inline fun PatternMatch.plusMatchOrNull(pattern: Pattern, value: Value): PatternMatch? =
	orNull
		.zipFold(value.fieldStack, pattern.value.fieldStack) { fieldOrNull, patternFieldOrNull ->
			when {
				this == null -> null
				patternFieldOrNull == null ->
					if (pattern.isAny)
						if (fieldOrNull == null) this
						else anyPlus(fieldOrNull)
					else
						if (fieldOrNull == null) this
						else null
				fieldOrNull == null -> null
				else ->
					if (fieldOrNull.matches(patternFieldOrNull)) plus(fieldOrNull)
					else null
			}
		}

inline fun Pattern.matchOrNull(value: Value): PatternMatch? =
	emptyMatch.plusMatchOrNull(this, value)

fun Value.matches(pattern: Pattern): Boolean =
	emptyMatch.plusMatchOrNull(pattern, this) != null

inline fun Field.matches(field: PatternField): Boolean =
	when (this) {
		is SentenceField -> field is SentencePatternField && sentence.matches(field.sentence)
		is FunctionField -> field is FunctionPatternField && function.pattern == field.function.pattern
		is DictionaryField -> field == _dictionary(anyPattern)
		is NativeField -> field == _native(anyPattern)
		is ChoiceField -> field == _choice(anyPattern)
		is LazyField -> field == _lazy(anyPattern)
		is EvaluatedField -> field == _evaluated(anyPattern)
	}

inline fun Sentence.matches(sentence: PatternSentence): Boolean =
	word == sentence.word && value.matches(sentence.pattern)

inline fun Literal.matches(field: PatternField): Boolean =
	when (this) {
		is StringLiteral -> field == _text(anyPattern)
		is NumberLiteral -> field == _number(anyPattern)
	}
