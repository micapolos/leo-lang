package leo25

val nativeContext: Context
	get() =
		context()
			.plus(textAppendTextScript, textAppendTextBody)
			.plus(numberAddNumberScript, numberAddNumberBody)
			.plus(numberSubtractNumberScript, numberSubtractNumberBody)
			.plus(numberMultiplyByNumberScript, numberMultiplyByNumberBody)
			.plus(getHashScript, getHashBody)
			.plus(anyIsAnyScript, isBody)
