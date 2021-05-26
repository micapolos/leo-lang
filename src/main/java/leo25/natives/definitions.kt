package leo25.natives

import leo14.*
import leo14.Number
import leo14.untyped.typed.loadClass
import leo16.nativeString
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
