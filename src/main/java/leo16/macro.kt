package leo16

import leo.base.notNullIf
import leo16.names.*

data class Macro(val patternValue: Value, val compiled: Compiled) {
	override fun toString() = asValue.toString()

	val asValue get() = patternValue.plus(_expands(compiled.bodyValue))

	fun apply(evaluated: Evaluated): Evaluated? =
		notNullIf(patternValue.matches(evaluated.value)) {
			compiled.invoke(evaluated.value).let { invoked ->
				evaluated.scope.emptyEvaluator.plus(invoked).evaluated
			}
		}
}

fun Value.expands(compiled: Compiled) = Macro(this, compiled)

fun Dictionary.macroOrNull(value: Value): Macro? =
	value.matchInfix(_expands) { lhs, rhs ->
		lhs.expands(compiled(rhs))
	}

