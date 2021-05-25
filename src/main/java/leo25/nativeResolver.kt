package leo25

val nativeResolver: Resolver
	get() =
		resolver()
			.plus(textAndTextScript, textAndTextBody)
			.plus(numberPlusNumberScript, numberPlusNumberBody)
			.plus(numberMinusNumberScript, numberMinusNumberBody)
			.plus(numberTimesNumberScript, numberTimesNumberBody)
