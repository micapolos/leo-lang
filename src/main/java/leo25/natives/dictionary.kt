package leo25.natives

import leo25.Dictionary
import leo25.plus
import leo25.dictionary

val nativeDictionary: Dictionary
	get() =
		dictionary()
			.plus(textAppendTextDefinition)
			.plus(textLengthDefinition)
			.plus(numberPlusNumberDefinition)
			.plus(numberMinusNumberDefinition)
			.plus(numberTimesNumberDefinition)
			.plus(textObjectJavaDefinition)
			.plus(javaObjectTextDefinition)
			.plus(textClassJavaDefinition)
			.plus(javaClassMethodNameTextDefinition)
			.plus(javaObjectInvokeJavaMethodDefinition)
