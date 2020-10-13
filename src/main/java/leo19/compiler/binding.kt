package leo19.compiler

import leo.base.notNullIf
import leo19.term.get
import leo19.term.invoke
import leo19.term.nullTerm
import leo19.term.term
import leo19.term.variable
import leo19.type.Arrow
import leo19.type.Struct
import leo19.type.StructType
import leo19.type.fieldTo
import leo19.type.indexedFieldOrNull
import leo19.type.isStatic
import leo19.type.nameOrNull
import leo19.type.struct
import leo19.typed.Typed
import leo19.typed.of

sealed class Binding
data class StructBinding(val struct: Struct) : Binding()
data class ArrowBinding(val arrow: Arrow) : Binding()

fun binding(struct: Struct): Binding = StructBinding(struct)
fun binding(arrow: Arrow): Binding = ArrowBinding(arrow)

fun Binding.resolveOrNull(typed: Typed, index: Int): Typed? =
	when (this) {
		is StructBinding ->
			typed.type.nameOrNull?.let { name ->
				struct.indexedFieldOrNull(name)?.let { indexedField ->
					if (indexedField.value.isStatic) nullTerm.of(struct(indexedField.value))
					else term(variable(index)).get(term(indexedField.index)).of(struct(indexedField.value))
				}
			}
		is ArrowBinding ->
			notNullIf(arrow.lhs == typed.type) {
				term(variable(index)).invoke(typed.term).of(arrow.rhs)
			}
	}
