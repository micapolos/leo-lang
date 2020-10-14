package leo19.compiler

import leo.base.notNullIf
import leo14.ScriptLine
import leo14.lineTo
import leo14.script
import leo19.term.get
import leo19.term.invoke
import leo19.term.nullTerm
import leo19.term.term
import leo19.term.variable
import leo19.type.Arrow
import leo19.type.Struct
import leo19.type.isStatic
import leo19.type.nameOrNull
import leo19.type.reflectScript
import leo19.type.type
import leo19.typed.Typed
import leo19.typed.indexedFieldOrNull
import leo19.typed.of

sealed class Binding {
	override fun toString() = reflect.toString()
}

data class StructBinding(val struct: Struct) : Binding() {
	override fun toString() = super.toString()
}

data class FunctionBinding(val arrow: Arrow) : Binding() {
	override fun toString() = super.toString()
}

data class ConstantBinding(val arrow: Arrow) : Binding() {
	override fun toString() = super.toString()
}

val Binding.reflect: ScriptLine
	get() =
		"binding" lineTo when (this) {
			is StructBinding -> script("struct" lineTo struct.reflectScript)
			is FunctionBinding -> script("function" lineTo arrow.reflectScript)
			is ConstantBinding -> script("constant" lineTo arrow.reflectScript)
		}

fun binding(struct: Struct): Binding = StructBinding(struct)
fun functionBinding(arrow: Arrow): Binding = FunctionBinding(arrow)
fun constantBinding(arrow: Arrow): Binding = ConstantBinding(arrow)

fun Binding.resolveOrNull(typed: Typed, index: Int): Typed? =
	when (this) {
		is StructBinding ->
			typed.type.nameOrNull?.let { name ->
				struct.indexedFieldOrNull(name)?.let { indexedField ->
					if (indexedField.value.isStatic) nullTerm.of(type(indexedField.value))
					else term(variable(index)).get(term(indexedField.index)).of(type(indexedField.value))
				}
			}
		is FunctionBinding ->
			notNullIf(arrow.lhs == typed.type) {
				term(variable(index)).invoke(typed.term).of(arrow.rhs)
			}
		is ConstantBinding ->
			notNullIf(arrow.lhs == typed.type) {
				term(variable(index)).of(arrow.rhs)
			}
	}
