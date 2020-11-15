package leo21.token.body

import leo.base.notNullIf
import leo14.lambda.arg
import leo14.lambda.invoke
import leo14.lambda.value.Value
import leo14.lambda.value.apply
import leo21.compiled.Compiled
import leo21.compiled.getOrNull
import leo21.compiled.make
import leo21.compiled.of
import leo21.evaluator.Evaluated
import leo21.evaluator.accessOrNull
import leo21.evaluator.make
import leo21.evaluator.of
import leo21.prim.Prim
import leo21.prim.runtime.apply
import leo21.type.Arrow
import leo21.type.onlyNameOrNull

sealed class Binding {
	data class Given(val given: leo21.token.body.Given) : Binding()
	data class Arrow(val arrow: leo21.type.Arrow) : Binding()
}

val Given.binding: Binding get() = Binding.Given(this)
val Arrow.binding: Binding get() = Binding.Arrow(this)

fun Binding.resolveOrNull(index: Int, param: Compiled): Compiled? =
	when (this) {
		is Binding.Given -> param.type.onlyNameOrNull?.let { name ->
			when (name) {
				"given" -> arg<Prim>(index).of(given.type).make("given")
				else -> arg<Prim>(index).of(given.type).make("given").getOrNull(name)
			}
		}
		is Binding.Arrow -> notNullIf(arrow.lhs == param.type) {
			arg<Prim>(index).invoke(param.term).of(arrow.rhs)
		}
	}

fun Binding.resolveOrNull(value: Value<Prim>, param: Evaluated): Evaluated? =
	when (this) {
		is Binding.Given -> param.type.onlyNameOrNull?.let { name ->
			when (name) {
				"given" -> value.of(given.type).make("given")
				else -> value.of(given.type).accessOrNull(name)
			}
		}
		is Binding.Arrow -> notNullIf(arrow.lhs == param.type) {
			value.apply(param.value, Prim::apply).of(arrow.rhs)
		}
	}
