package leo21.token.body

import leo.base.notNullIf
import leo14.ScriptLine
import leo14.Scriptable
import leo14.anyReflectScriptLine
import leo14.lambda.arg
import leo14.lambda.invoke
import leo14.lambda.value.Value
import leo14.lambda.value.apply
import leo14.lineTo
import leo14.script
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

sealed class Binding : Scriptable() {
	override val reflectScriptLine: ScriptLine
		get() = "binding" lineTo script(
			when (this) {
				is Given -> given.anyReflectScriptLine
				is Arrow -> arrow.anyReflectScriptLine
			})

	data class Given(val given: leo21.token.body.Given) : Binding() {
		override fun toString() = super.toString()
	}

	data class Arrow(val arrow: leo21.type.Arrow) : Binding() {
		override fun toString() = super.toString()
	}
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
