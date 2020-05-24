package leo16.lambda

val TypeChoice.isStatic: Boolean
	get() =
		onlyCaseOrNull?.isStatic ?: false

val TypeCase.isStatic: Boolean
	get() =
		when (this) {
			EmptyTypeCase -> true
			is LinkTypeCase -> link.type.isStatic && link.field.isStatic
		}

val TypeField.isStatic: Boolean
	get() =
		when (this) {
			is SentenceTypeField -> sentence.isStatic
			is FunctionTypeField -> function.isStatic
			is NativeTypeField -> false
		}

val TypeSentence.isStatic
	get() =
		type.isStatic

val TypeFunction.isStatic: Boolean
	get() =
		output.isStatic
