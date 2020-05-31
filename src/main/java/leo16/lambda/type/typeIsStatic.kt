package leo16.lambda.type

val TypeBody.isStatic: Boolean
	get() =
		when (this) {
			EmptyTypeBody -> true
			is LinkTypeBody -> link.previousType.isStatic && link.lastSentence.isStatic
			is FunctionTypeBody -> function.isStatic
			is AlternativeTypeBody -> false
			is NativeTypeBody -> false
			is LazyTypeBody -> lazy.isStatic
			is RepeatingTypeBody -> false
			RepeatTypeBody -> false
		}

val TypeSentence.isStatic
	get() =
		type.isStatic

val TypeFunction.isStatic: Boolean
	get() =
		resultType.isStatic

val TypeLazy.isStatic: Boolean
	get() =
		resultType.isStatic
