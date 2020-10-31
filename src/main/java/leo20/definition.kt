package leo20

import leo.base.notNullIf
import leo14.Script

sealed class Definition
data class FunctionDefinition(val pattern: Pattern, val function: Function, val isRecursive: Boolean) : Definition()
data class ValueDefinition(val value: Value) : Definition()

fun Definition.resolveOrNull(param: Value): Value? =
	when (this) {
		is FunctionDefinition -> notNullIf(param.matches(pattern)) {
			if (isRecursive) function.applyRecursively(pattern, param)
			else function.apply(param)
		}
		is ValueDefinition -> null
	}

fun Definition.getOrNull(script: Script): Value? =
	when (this) {
		is FunctionDefinition -> null
		is ValueDefinition -> value("given" lineTo value).getOrNull(script)
	}

val numberPlusDefinition: Definition =
	FunctionDefinition(
		pattern(
			numberPatternLine,
			"plus" lineTo pattern(numberPatternLine)),
		emptyDictionary.function(NumberPlusBody),
		isRecursive = false)

val numberMinusDefinition: Definition =
	FunctionDefinition(
		pattern(
			numberPatternLine,
			"minus" lineTo pattern(numberPatternLine)),
		emptyDictionary.function(NumberMinusBody),
		isRecursive = false)

val numberEqualsDefinition: Definition =
	FunctionDefinition(
		pattern(
			numberPatternLine,
			"equals" lineTo pattern(numberPatternLine)),
		emptyDictionary.function(NumberEqualsBody),
		isRecursive = false)

val textAppendDefinition: Definition =
	FunctionDefinition(
		pattern(
			textPatternLine,
			"append" lineTo pattern(textPatternLine)),
		emptyDictionary.function(StringAppendBody),
		isRecursive = false)
