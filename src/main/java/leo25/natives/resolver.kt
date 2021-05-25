package leo25

val nativeResolver: Resolver
	get() =
		resolver()
			.plus(textAppendTextDefinition)
			.plus(numberPlusNumberDefinition)
			.plus(numberMinusNumberDefinition)
			.plus(numberTimesNumberDefinition)
