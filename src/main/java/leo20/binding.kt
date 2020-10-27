package leo20

import leo.base.notNullIf

sealed class Binding
data class ValueBinding(val pattern: Pattern, val value: Value) : Binding()
data class FunctionBinding(val pattern: Pattern, val function: Function, val isRecursive: Boolean) : Binding()

val Line.binding: Binding get() = ValueBinding(pattern(selectName lineTo pattern()), value(this))

fun Binding.resolveOrNull(param: Value): Value? =
	when (this) {
		is ValueBinding ->
			notNullIf(param.matches(pattern)) { value }
		is FunctionBinding ->
			notNullIf(param.matches(pattern)) {
				if (isRecursive) function.applyRecursively(pattern, param)
				else function.apply(param)
			}
	}


val numberPlusBinding: Binding =
	FunctionBinding(
		pattern(
			numberPatternLine,
			"plus" lineTo pattern(numberPatternLine)),
		emptyScope.function(NumberPlusBody),
		isRecursive = false)

val numberMinusBinding: Binding =
	FunctionBinding(
		pattern(
			numberPatternLine,
			"minus" lineTo pattern(numberPatternLine)),
		emptyScope.function(NumberMinusBody),
		isRecursive = false)

val numberEqualsBinding: Binding =
	FunctionBinding(
		pattern(
			numberPatternLine,
			"equals" lineTo pattern(numberPatternLine)),
		emptyScope.function(NumberEqualsBody),
		isRecursive = false)
