package leo20

import leo.base.notNullIf

data class Definition(val pattern: Pattern, val function: Function, val isRecursive: Boolean)

fun Definition.resolveOrNull(param: Value): Value? =
	notNullIf(param.matches(pattern)) {
		if (isRecursive) function.applyRecursively(pattern, param)
		else function.apply(param)
	}

val numberPlusDefinition: Definition =
	Definition(
		pattern(
			numberPatternLine,
			"plus" lineTo pattern(numberPatternLine)),
		emptyScope.function(NumberPlusBody),
		isRecursive = false)

val numberMinusDefinition: Definition =
	Definition(
		pattern(
			numberPatternLine,
			"minus" lineTo pattern(numberPatternLine)),
		emptyScope.function(NumberMinusBody),
		isRecursive = false)

val numberEqualsDefinition: Definition =
	Definition(
		pattern(
			numberPatternLine,
			"equals" lineTo pattern(numberPatternLine)),
		emptyScope.function(NumberEqualsBody),
		isRecursive = false)

val textAppendDefinition: Definition =
	Definition(
		pattern(
			textPatternLine,
			"append" lineTo pattern(textPatternLine)),
		emptyScope.function(StringAppendBody),
		isRecursive = false)
