package leo15.type

val Type.isStatic: Boolean
	get() =
		when (this) {
			EmptyType -> true
			is LinkType -> link.isStatic
			is RepeatingType -> false
			is RecursiveType -> false
			RecurseType -> false
		}

val TypeLink.isStatic: Boolean
	get() =
		lhs.isStatic && choice.isStatic

val Choice.isStatic: Boolean
	get() =
		when (this) {
			is LineChoice -> line.isStatic
			is LinkChoice -> false
		}

val TypeLine.isStatic: Boolean
	get() =
		when (this) {
			is LiteralTypeLine -> true
			is FieldTypeLine -> field.isStatic
			is ArrowTypeLine -> false
			JavaTypeLine -> false
		}

val TypeField.isStatic: Boolean
	get() =
		rhs.isStatic