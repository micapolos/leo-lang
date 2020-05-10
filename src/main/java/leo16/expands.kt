package leo16

import leo.base.notNullIf
import leo16.names.*

data class Expands(val pattern: Pattern, val function: Function) {
	override fun toString() = asValue.toString()
	val asValue get() = pattern.asValue.plus(_expands(function.bodyValue))
}

fun Pattern.expands(function: Function) = Expands(this, function)

fun Dictionary.expandsOrNull(value: Value): Expands? =
	value.matchInfix(_expands) { lhs, rhs ->
		lhs.pattern.expands(function(rhs))
	}

fun Expands.apply(evaluated: Evaluated): Evaluated? =
	pattern.matchOrNull(evaluated.value)?.let { match ->
		function.invoke(match).let { invoked ->
			evaluated.scope.emptyEvaluator.plus(invoked).evaluated
		}
	}

