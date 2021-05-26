package leo25.natives

import leo14.*
import leo14.Number
import leo14.untyped.typed.loadClass
import leo15.arrayName
import leo16.native.nativeObjectClassDefinition
import leo25.*

val textAppendTextDefinition
	get() =
		nativeDefinition(
			textName, { nativeText },
			appendName,
			textName, { nativeText },
			{ nativeValue }, String::plus
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

val nullObjectJavaDefinition
	get() =
		nativeDefinition(
			script(
				objectName lineTo script(nullName),
				javaName lineTo script()
			)
		) {
			value(javaName fieldTo value(objectName fieldTo rhs(native(null))))
		}


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

val numberIntegerObjectJavaDefinition
	get() =
		nativeDefinition(
			script(
				objectName lineTo script(integerName lineTo script(numberName lineTo script(anyName))),
				javaName lineTo script()
			)
		) {
			value(
				javaName fieldTo value(
					objectName fieldTo rhs(
						native(
							nativeValue(objectName).nativeValue(integerName)
								.nativeValue(numberName).nativeNumber.bigDecimal.intValueExact()
						)
					)
				)
			)
		}

val javaObjectNumberDefinition
	get() =
		nativeDefinition(
			script(
				javaName lineTo script(objectName lineTo script(anyName)),
				numberName lineTo script()
			)
		) {
			value(field(literal(nativeValue(javaName).nativeValue(objectName).nativeObject as Int)))
		}

val arrayObjectJavaDefinition
	get() =
		nativeDefinition(
			script(
				objectName lineTo script(arrayName lineTo script(anyName)),
				javaName lineTo script()
			)
		) {
			value(
				javaName fieldTo value(
					objectName fieldTo rhs(
						native(nativeValue(objectName).nativeValue(arrayName).nativeArray)
					)
				)
			)
		}

val textClassJavaDefinition
	get() =
		nativeDefinition(
			script(
				objectName lineTo script(
					className lineTo script(
						nameName lineTo script(
							textName lineTo script(anyName)
						)
					)
				),
				javaName lineTo script()
			)
		) {
			value(
				javaName fieldTo value(
					objectName fieldTo rhs(
						native(
							this
								.nativeValue(objectName)
								.nativeValue(className)
								.nativeValue(nameName)
								.nativeValue(textName)
								.nativeText
								.loadClass
						)
					)
				)
			)
		}

val javaClassMethodNameTextDefinition
	get() =
		nativeDefinition(
			script(
				className lineTo script(
					javaName lineTo script(
						objectName lineTo script(anyName)
					)
				),
				methodName lineTo script(
					nameName lineTo script(
						textName lineTo script(anyName)
					),
					typesName lineTo script(
						javaName lineTo script(
							objectName lineTo script(anyName)
						)
					)
				)
			)
		) {
			value(
				javaName fieldTo value(
					methodName fieldTo rhs(
						native(
							this
								.nativeValue(className)
								.nativeValue(javaName)
								.nativeValue(objectName)
								.nativeClass
								.getMethod(
									this
										.nativeValue(methodName)
										.nativeValue(nameName)
										.nativeValue(textName)
										.nativeText,
									*this
										.nativeValue(methodName)
										.nativeValue(typesName)
										.nativeValue(javaName)
										.nativeValue(objectName)
										.nativeObject
										.run { this as Array<*> }
										.toList()
										.run { this as List<Class<*>> }
										.toTypedArray())))))
		}

val javaObjectInvokeJavaMethodDefinition
	get() =
		nativeDefinition(
			script(
				javaName lineTo script(objectName lineTo script(anyName)),
				invokeName lineTo script(
					javaName lineTo script(methodName lineTo script(anyName)),
					argsName lineTo script(javaName lineTo script(objectName lineTo script(anyName)))
				)
			)
		) {
			value(
				javaName fieldTo value(
					objectName fieldTo rhs(
						native(
							this
								.nativeValue(invokeName)
								.nativeValue(javaName)
								.nativeValue(methodName)
								.nativeMethod
								.invoke(
									this
										.nativeValue(javaName)
										.nativeValue(objectName)
										.nativeObject,
									*this
										.nativeValue(invokeName)
										.nativeValue(argsName)
										.nativeValue(javaName)
										.nativeValue(objectName)
										.nativeObject as Array<*>
								)
						)
					)
				)
			)
		}
