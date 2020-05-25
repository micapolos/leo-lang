package leo16.lambda.type

val TypeBody.isStatic: Boolean
	get() =
		when (this) {
			EmptyTypeBody -> true
			is LinkTypeBody -> link.previousType.isStatic && link.lastField.isStatic
			is AlternativeTypeBody -> false
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
