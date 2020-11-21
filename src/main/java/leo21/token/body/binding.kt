package leo21.token.body

import leo.base.notNullIf
import leo13.Empty
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
import leo21.type.Type
import leo21.type.onlyNameOrNull
import javax.swing.border.EmptyBorder

sealed class Binding : Scriptable() {
	override val reflectScriptLine: ScriptLine
		get() = "binding" lineTo script(
			when (this) {
				is Given -> given.anyReflectScriptLine
				is Function ->
					"function" lineTo script(
						"key" lineTo script(keyType.reflectScriptLine),
						"value" lineTo script(valueType.reflectScriptLine))
				is Constant ->
					"constant" lineTo script(
						"key" lineTo script(keyType.reflectScriptLine),
						"value" lineTo script(valueType.reflectScriptLine))
				is Empty -> "empty" lineTo script()
			})

	data class Given(val given: leo21.token.body.Given) : Binding() {
		override fun toString() = super.toString()
	}

	data class Function(val keyType: Type, val valueType: Type) : Binding() {
		override fun toString() = super.toString()
	}

	data class Constant(val keyType: Type, val valueType: Type) : Binding() {
		override fun toString() = super.toString()
	}

	data class Empty(val empty: leo13.Empty) : Binding() {
		override fun toString() = super.toString()
	}
}

val Empty.binding: Binding get() = Binding.Empty(this)
val Given.binding: Binding get() = Binding.Given(this)
fun Type.functionBinding(type: Type): Binding = Binding.Function(this, type)
fun Type.constantBinding(type: Type): Binding = Binding.Constant(this, type)

fun Binding.resolveOrNull(index: Int, param: Compiled): Compiled? =
	when (this) {
		is Binding.Given -> param.type.onlyNameOrNull?.let { name ->
			when (name) {
				"given" -> arg<Prim>(index).of(given.type).make("given")
				else -> arg<Prim>(index).of(given.type).make("given").getOrNull(name)
			}
		}
		is Binding.Function -> notNullIf(keyType == param.type) {
			arg<Prim>(index).invoke(param.term).of(valueType)
		}
		is Binding.Constant -> notNullIf(keyType == param.type) {
			arg<Prim>(index).of(valueType)
		}
		is Binding.Empty -> null
	}

fun Binding.resolveOrNull(value: Value<Prim>, param: Evaluated): Evaluated? =
	when (this) {
		is Binding.Given -> param.type.onlyNameOrNull?.let { name ->
			when (name) {
				"given" -> value.of(given.type).make("given")
				else -> value.of(given.type).accessOrNull(name)
			}
		}
		is Binding.Function -> notNullIf(keyType == param.type) {
			value.apply(param.value, Prim::apply).of(valueType)
		}
		is Binding.Constant -> notNullIf(keyType == param.type) {
			value.of(valueType)
		}
		is Binding.Empty -> null
	}
