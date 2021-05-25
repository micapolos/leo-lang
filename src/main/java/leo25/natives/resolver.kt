package leo25.natives

import leo25.Dictionary
import leo25.plus
import leo25.resolver

val nativeDictionary: Dictionary
	get() =
		resolver()
			.plus(textAppendTextDefinition)
			.plus(numberPlusNumberDefinition)
			.plus(numberMinusNumberDefinition)
			.plus(numberTimesNumberDefinition)
