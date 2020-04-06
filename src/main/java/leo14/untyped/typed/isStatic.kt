package leo14.untyped.typed

val Type.isStatic: Boolean
	get() =
		when (this) {
			is EmptyType -> true
			is LinkType -> link.isStatic
		}

val TypeLink.isStatic: Boolean
	get() =
		choice.isStatic && lhs.isStatic

val Choice.isStatic: Boolean
	get() =
		when (this) {
			is EmptyChoice -> true
			is LinkChoice -> link.isStatic
		}

val ChoiceLink.isStatic: Boolean
	get() =
		line.isStatic && lhs is EmptyChoice

val TypeLine.isStatic: Boolean
	get() =
		when (this) {
			is LiteralTypeLine -> true
			is NativeTypeLine -> false
			is FieldTypeLine -> field.isStatic
		}

val TypeField.isStatic: Boolean
	get() =
		rhs.isStatic
