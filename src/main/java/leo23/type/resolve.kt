package leo23.type

val Types.resolveOrNull: Type?
	get() =
		when {
			this == "boolean" struct fields() -> booleanType
			this == "text" struct fields() -> textType
			this == "number" struct fields() -> numberType
			else -> null
		}
