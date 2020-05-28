package leo16

import leo13.fold
import leo13.reverse
import leo16.names.*

val Value.pattern: Pattern
	get() =
		emptyPattern.fold(fieldStack.reverse) { plus(it) }

fun Pattern.plus(field: Field): Pattern =
	if (field == _any() && isEmpty) anyPattern
	else plus(field.patternField)

val Field.patternField: PatternField
	get() =
		when (this) {
			is SentenceField -> sentence.patternField
			is FunctionField -> function.asPatternField.patternField
			is NativeField -> native.nativePatternField
			is LazyField -> lazy.patternField
			is EvaluatedField -> evaluated.patternField
		}

val Sentence.patternField: PatternField
	get() =
		when (word) {
			_function -> value.pattern.function.field
			else -> exactPatternField
		}

val Lazy.patternField: PatternField
	get() =
		_lazy.invoke(compiled.bodyValue.pattern)

val Evaluated.patternField: PatternField
	get() =
		asField.patternField

val Sentence.exactPatternField: PatternField
	get() =
		word.invoke(value.pattern)
