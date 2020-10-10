package leo19.typed

import leo19.type.Type
import leo19.type.native
import leo19.type.struct
import leo19.type.to
import leo19.value.ListValue
import leo19.value.Value

data class Typed(val value: Value, val type: Type)
data class TypedField(val name: String, val value: Typed)

fun typed(vararg typedFields: TypedField) =
	when (typedFields.size) {
		1 -> typedFields[0].let { typedField ->
			Typed(typedField.value.value, native(struct(typedField.name to typedField.value.type)))
		}
		else -> Typed(
			ListValue(typedFields.map { it.value.value }),
			struct(*typedFields.map { it.name to it.value.type }.toTypedArray()))
	}
