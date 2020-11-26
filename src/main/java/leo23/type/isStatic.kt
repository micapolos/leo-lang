package leo23.type

val Type.isStatic: Boolean
	get() =
		when (this) {
			BooleanType -> false
			TextType -> false
			NumberType -> false
			is ArrowType -> returnType.isStatic
			is StructType -> fields.all { it.isStatic }
			is ChoiceType -> false
		}