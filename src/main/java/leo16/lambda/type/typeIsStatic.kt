package leo16.lambda.type

val TypeBody.isStatic: Boolean
	get() =
		when (this) {
			EmptyTypeBody -> true
			is LinkTypeBody -> link.previousType.isStatic && link.lastField.isStatic
			is FunctionTypeBody -> function.isStatic
			is AlternativeTypeBody -> false
			is NativeTypeBody -> false
		}

val TypeField.isStatic: Boolean
	get() =
		when (this) {
			is SentenceTypeField -> sentence.isStatic
			is NativeTypeField -> false
		}

val TypeSentence.isStatic
	get() =
		type.isStatic

val TypeFunction.isStatic: Boolean
	get() =
		output.isStatic
