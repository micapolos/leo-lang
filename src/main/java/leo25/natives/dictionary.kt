package leo25.natives

import leo25.Dictionary
import leo25.dictionary
import leo25.plus

val nativeDictionary: Dictionary
	get() =
		dictionary()
			.plus(numberPlusNumberDefinition)
			.plus(numberMinusNumberDefinition)
			.plus(numberTimesNumberDefinition)

			.plus(nullObjectJavaDefinition)
			.plus(arrayObjectJavaDefinition)

			.plus(textObjectJavaDefinition)
			.plus(javaObjectTextDefinition)

			.plus(numberIntegerObjectJavaDefinition)
			.plus(javaObjectNumberDefinition)

			.plus(textClassJavaDefinition)
			.plus(javaClassMethodNameTextDefinition)
			.plus(javaObjectInvokeJavaMethodDefinition)
