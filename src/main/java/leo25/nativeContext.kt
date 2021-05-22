package leo25

val nativeDictionary: Dictionary
	get() =
		dictionary()
			.plus(textAppendTextScript, textAppendTextBody)
			.plus(numberAddNumberScript, numberAddNumberBody)
			.plus(numberSubtractNumberScript, numberSubtractNumberBody)
			.plus(numberMultiplyByNumberScript, numberMultiplyByNumberBody)
			.plus(getHashScript, getHashBody)
			.plus(anyIsAnyScript, isBody)
