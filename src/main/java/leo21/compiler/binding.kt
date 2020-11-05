package leo21.compiler

import leo.base.notNullIf
import leo14.ScriptLine
import leo14.Scriptable
import leo14.lambda.arg
import leo14.lambda.invoke
import leo14.lineTo
import leo14.script
import leo21.prim.Prim
import leo21.type.Arrow
import leo21.type.Type
import leo21.type.onlyNameOrNull
import leo21.compiled.Compiled
import leo21.compiled.getOrNull
import leo21.compiled.make

sealed class Binding : Scriptable() {
	override val reflectScriptLine: ScriptLine
		get() = "binding" lineTo script(when (this) {
			is ConstantBinding -> "constant" lineTo script(arrow.reflectScriptLine)
			is FunctionBinding -> "function" lineTo script(arrow.reflectScriptLine)
			is GivenBinding -> "given" lineTo script(type.reflectScriptLine)
		})
}

data class ConstantBinding(val arrow: Arrow) : Binding() {
	override fun toString() = super.toString()
}

data class FunctionBinding(val arrow: Arrow) : Binding() {
	override fun toString() = super.toString()
}

data class GivenBinding(val type: Type) : Binding() {
	override fun toString() = super.toString()
}

fun constantBinding(arrow: Arrow): Binding = ConstantBinding(arrow)
fun functionBinding(arrow: Arrow): Binding = FunctionBinding(arrow)
fun givenBinding(type: Type): Binding = GivenBinding(type)

fun Binding.applyOrNull(index: Int, compiled: Compiled): Compiled? =
	when (this) {
		is ConstantBinding -> notNullIf(compiled.type == arrow.lhs) {
			Compiled(arg(index), arrow.rhs)
		}
		is FunctionBinding -> notNullIf(compiled.type == arrow.lhs) {
			Compiled(arg<Prim>(index).invoke(compiled.term), arrow.rhs)
		}
		is GivenBinding -> compiled.type.onlyNameOrNull?.let { name ->
			when (name) {
				"given" -> Compiled(arg(index), type).make("given")
				else -> Compiled(arg(index), type).make("given").getOrNull(name)
			}
		}
	}
