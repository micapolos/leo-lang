package leo20

import leo.base.notNullIf
import leo.base.runIf

data class Binding(val pattern: Pattern, val value: Value, val isFunction: Boolean)

val Line.binding get() = Binding(pattern(selectName lineTo pattern()), value(this), false)

fun Binding.resolveOrNull(param: Value): Value? =
	notNullIf(param.matches(pattern)) {
		value.runIf(isFunction) { apply(param) }
	}

val numberPlusBinding =
	Binding(
		pattern(
			numberPatternLine,
			"plus" lineTo pattern(numberPatternLine)),
		value(line(emptyScope.function(NumberPlusBody))),
		true)