package leo21.token.body

import leo.base.notNullIf
import leo14.lambda.arg
import leo14.lambda.invoke
import leo21.compiled.Compiled
import leo21.compiled.getOrNull
import leo21.compiled.make
import leo21.compiled.of
import leo21.prim.Prim
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

