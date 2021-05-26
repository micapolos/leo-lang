package leo25.natives

import leo14.*
import leo14.Number
import leo14.untyped.typed.loadClass
import leo25.*

val textAppendTextDefinition
	get() =
		nativeDefinition(
			textName, { nativeText },
			appendName,
			textName, { nativeText },
			{ nativeValue }, String::plus
		)

val textLengthDefinition
	get() =
		nativeDefinition(
			textName, { nativeText },
			lengthName, { nativeValue },
			lengthName, String::lengthNumber
		)

val numberPlusNumberDefinition
	get() =
		nativeDefinition(
			numberName, { nativeNumber },
			plusName,
			numberName, { nativeNumber },
			{ nativeValue }, Number::plus
		)

val numberMinusNumberDefinition
	get() =
		nativeDefinition(
			numberName, { nativeNumber },
			minusName,
			numberName, { nativeNumber },
			{ nativeValue }, Number::minus
		)

val numberTimesNumberDefinition
	get() =
		nativeDefinition(
			numberName, { nativeNumber },
			timesName,
			numberName, { nativeNumber },
			{ nativeValue }, Number::times
		)

val textObjectJavaDefinition
	get() =
		nativeDefinition(
			script(
				objectName lineTo script(textName lineTo script(anyName)),
				javaName lineTo script()
			)
		) {
			value(
				javaName fieldTo value(
					objectName fieldTo rhs(
						native(nativeValue(objectName).nativeValue(textName).nativeText)
					)
				)
			)
		}

val javaObjectTextDefinition
	get() =
		nativeDefinition(
			script(
				javaName lineTo script(objectName lineTo script(anyName)),
				textName lineTo script()
			)
		) {
			value(field(literal(nativeValue(javaName).nativeValue(objectName).nativeObject as String)))
		}

val numberIntegerJavaDefinition
	get() =
		nativeDefinition(
			script(
				integerName lineTo script(numberName lineTo script(anyName)),
				javaName lineTo script()
			)
		) {
			value(
				javaName fieldTo value(
					integerName fieldTo rhs(
						native(nativeValue(integerName).nativeValue(numberName).nativeNumber.bigDecimal.intValueExact())
					)
				)
			)
		}

val javaIntegerNumberDefinition
	get() =
		nativeDefinition(
			script(
				javaName lineTo script(integerName lineTo script(anyName)),
				numberName lineTo script()
			)
		) {
			value(field(literal(nativeValue(javaName).nativeValue(integerName).nativeObject as Int)))
		}

val textClassJavaDefinition
	get() =
		nativeDefinition(
			script(
				className lineTo script(textName lineTo script(anyName)),
				javaName lineTo script()
			)
		) {
			value(
				javaName fieldTo value(
					className fieldTo rhs(
						native(nativeValue(className).nativeValue(textName).nativeText.loadClass)
					)
				)
			)
		}

val javaClassMethodNameTextDefinition
	get() =
		nativeDefinition(
			script(
				javaName lineTo script(className lineTo script(anyName)),
				methodName lineTo script(textName lineTo script(anyName))
			)
		) {
			value(
				javaName fieldTo value(
					methodName fieldTo rhs(
						native(
							nativeValue(javaName).nativeValue(className).nativeClass.getMethod(
								nativeValue(methodName).nativeValue(textName).nativeText
							)
						)
					)
				)
			)
		}

val javaObjectInvokeJavaMethodDefinition
	get() =
		nativeDefinition(
			script(
				javaName lineTo script(objectName lineTo script(anyName)),
				invokeName lineTo script(javaName lineTo script(methodName lineTo script(anyName)))
			)
		) {
			value(
				javaName fieldTo value(
					objectName fieldTo rhs(
						native(
							nativeValue(invokeName).nativeValue(javaName).nativeValue(methodName).nativeMethod.invoke(
								nativeValue(javaName).nativeValue(objectName).nativeObject
							)
						)
					)
				)
			)
		}
