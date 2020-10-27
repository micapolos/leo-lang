package leo20

import leo.base.ifOrNull
import leo.base.notNullIf
import leo13.linkOrNull

sealed class Binding
data class ValueBinding(val pattern: Pattern, val value: Value) : Binding()
data class FunctionBinding(val pattern: Pattern, val function: Function) : Binding()
data class RecurseBinding(val function: Function) : Binding()

val Line.binding: Binding get() = ValueBinding(pattern(selectName lineTo pattern()), value(this))

fun Binding.resolveOrNull(param: Value): Value? =
	when (this) {
		is ValueBinding ->
			notNullIf(param.matches(pattern)) { value }
		is FunctionBinding ->
			notNullIf(param.matches(pattern)) { function.apply(param) }
		is RecurseBinding ->
			param.lineStack.linkOrNull?.let { link ->
				(link.value as? FieldLine)?.field?.let { field ->
					ifOrNull(field.name == "recurse") {
						function.apply(Value(link.stack))
					}
				}
			}
	}


val numberPlusBinding: Binding =
	FunctionBinding(
		pattern(
			numberPatternLine,
			"plus" lineTo pattern(numberPatternLine)),
		emptyScope.function(NumberPlusBody))

val numberMinusBinding: Binding =
	FunctionBinding(
		pattern(
			numberPatternLine,
			"minus" lineTo pattern(numberPatternLine)),
		emptyScope.function(NumberMinusBody))

val numberEqualsBinding: Binding =
	FunctionBinding(
		pattern(
			numberPatternLine,
			"equals" lineTo pattern(numberPatternLine)),
		emptyScope.function(NumberEqualsBody))
