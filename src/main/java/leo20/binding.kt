package leo20

import leo.base.notNullIf

data class Binding(val pattern: Pattern, val function: Function, val isRecursive: Boolean)

fun Binding.resolveOrNull(param: Value): Value? =
	notNullIf(param.matches(pattern)) {
		if (isRecursive) function.applyRecursively(pattern, param)
		else function.apply(param)
	}

val numberPlusBinding: Binding =
	Binding(
		pattern(
			numberPatternLine,
			"plus" lineTo pattern(numberPatternLine)),
		emptyScope.function(NumberPlusBody),
		isRecursive = false)

val numberMinusBinding: Binding =
	Binding(
		pattern(
			numberPatternLine,
			"minus" lineTo pattern(numberPatternLine)),
		emptyScope.function(NumberMinusBody),
		isRecursive = false)

val numberEqualsBinding: Binding =
	Binding(
		pattern(
			numberPatternLine,
			"equals" lineTo pattern(numberPatternLine)),
		emptyScope.function(NumberEqualsBody),
		isRecursive = false)

val textAppendBinding: Binding =
	Binding(
		pattern(
			textPatternLine,
			"append" lineTo pattern(textPatternLine)),
		emptyScope.function(StringAppendBody),
		isRecursive = false)
