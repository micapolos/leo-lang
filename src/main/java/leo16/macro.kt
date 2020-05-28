package leo16

import leo.base.notNullIf
import leo16.names.*

data class Macro(val pattern: Pattern, val compiled: Compiled) {
	override fun toString() = asValue.toString()

	val asValue get() = pattern.asValue.plus(_expands(compiled.bodyValue))

	fun apply(evaluated: Evaluated): Evaluated? =
		notNullIf(pattern.isMatching(evaluated.value)) {
			compiled.invoke(evaluated.value).let { invoked ->
				evaluated.scope.emptyEvaluator.plus(invoked).evaluated
			}
		}
}

fun Pattern.expands(compiled: Compiled) = Macro(this, compiled)

fun Dictionary.macroOrNull(value: Value): Macro? =
	value.matchInfix(_expands) { lhs, rhs ->
		lhs.pattern.expands(compiled(rhs))
	}

