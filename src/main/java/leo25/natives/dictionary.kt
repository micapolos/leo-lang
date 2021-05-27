package leo25.natives

import leo25.Dictionary
import leo25.dictionary
import leo25.plus

val nativeDictionary: Dictionary
	get() =
		dictionary()
			.plus(nullObjectJavaDefinition)
			.plus(trueObjectJavaDefinition)
			.plus(falseObjectJavaDefinition)

			.plus(javaObjectClassDefinition)
			.plus(arrayObjectJavaDefinition)

			.plus(textObjectJavaDefinition)
			.plus(javaObjectTextDefinition)

			.plus(numberObjectJavaDefinition)
			.plus(javaObjectNumberDefinition)

			.plus(numberIntegerObjectJavaDefinition)
			.plus(javaObjectIntegerNumberDefinition)

			.plus(textClassJavaDefinition)

			.plus(javaClassFieldDefinition)
			.plus(javaFieldGetDefinition)

			.plus(javaClassMethodNameTextDefinition)
			.plus(javaObjectInvokeJavaMethodDefinition)
