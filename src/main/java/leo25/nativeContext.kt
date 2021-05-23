package leo25

val nativeResolver: Resolver
	get() =
		resolver()
			.plus(textAppendTextScript, textAppendTextBody)
			.plus(numberAddNumberScript, numberAddNumberBody)
			.plus(numberSubtractNumberScript, numberSubtractNumberBody)
			.plus(numberMultiplyByNumberScript, numberMultiplyByNumberBody)
