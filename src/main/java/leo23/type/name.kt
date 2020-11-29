package leo23.type

import leo.base.notNullIf

val Type.name: String
	get() =
		when (this) {
			BooleanType -> "boolean"
			TextType -> "text"
			NumberType -> "number"
			is ArrowType -> "function"
			is StructType -> name
			is ChoiceType -> name
		}

val Type.onlyNameOrNull: String?
	get() =
		(this as? StructType)?.run {
			notNullIf(fields.isEmpty()) {
				name
			}
		}