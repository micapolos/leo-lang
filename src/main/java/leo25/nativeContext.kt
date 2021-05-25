package leo25

val nativeResolver: Resolver
	get() =
		resolver()
			.plus(textAndTextScript, textAndTextBody)
			.plus(numberAddNumberScript, numberAddNumberBody)
			.plus(numberSubtractNumberScript, numberSubtractNumberBody)
			.plus(numberMultiplyByNumberScript, numberMultiplyByNumberBody)
