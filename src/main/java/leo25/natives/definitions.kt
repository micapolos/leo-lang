package leo25.natives

import leo14.Number
import leo14.lineTo
import leo14.literal
import leo14.script
import leo14.untyped.typed.loadClass
import leo15.arrayName
import leo25.*
import java.lang.reflect.Field
import java.lang.reflect.Method

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

val trueObjectJavaDefinition
	get() =
		nativeDefinition(
			script(
				objectName lineTo script(trueName),
				javaName lineTo script()
			)
		) {
			value(javaName fieldTo value(objectName fieldTo rhs(native(true))))
		}

val falseObjectJavaDefinition
	get() =
		nativeDefinition(
			script(
				objectName lineTo script(falseName),
				javaName lineTo script()
			)
		) {
			value(javaName fieldTo value(objectName fieldTo rhs(native(false))))
		}

val javaObjectClassDefinition
	get() =
		nativeDefinition(
			script(
				javaName lineTo script(objectName lineTo script(anyName)),
				getName lineTo script(className lineTo script())
			)
		) {
			value(
				javaName fieldTo value(
					objectName fieldTo rhs(
						native(
							this
								.nativeValue(javaName)
								.nativeValue(objectName)
								.nativeObject!!
								.javaClass
						)
					)
				)
			)
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

val numberObjectJavaDefinition
	get() =
		nativeDefinition(
			script(
				objectName lineTo script(numberName lineTo script(anyName)),
				javaName lineTo script()
			)
		) {
			value(
				javaName fieldTo value(
					objectName fieldTo rhs(
						native(
							nativeValue(objectName).nativeValue(numberName).nativeNumber
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
			value(field(literal(nativeValue(javaName).nativeValue(objectName).nativeObject as Number)))
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

val javaObjectIntegerNumberDefinition
	get() =
		nativeDefinition(
			script(
				integerName lineTo script(javaName lineTo script(objectName lineTo script(anyName))),
				numberName lineTo script()
			)
		) {
			value(field(literal(nativeValue(integerName).nativeValue(javaName).nativeValue(objectName).nativeObject as Int)))
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

val javaClassFieldDefinition
	get() =
		nativeDefinition(
			script(
				className lineTo script(
					javaName lineTo script(
						objectName lineTo script(anyName)
					)
				),
				fieldName lineTo script(
					nameName lineTo script(
						textName lineTo script(anyName)
					)
				)
			)
		) {
			value(
				fieldName fieldTo value(
					javaName fieldTo value(
						objectName fieldTo rhs(
							native(
								this
									.nativeValue(className)
									.nativeValue(javaName)
									.nativeValue(objectName)
									.nativeClass
									.getField(
										this
											.nativeValue(fieldName)
											.nativeValue(nameName)
											.nativeValue(textName)
											.nativeText
									)
							)
						)
					)
				)
			)
		}

val javaFieldGetDefinition
	get() =
		nativeDefinition(
			script(
				fieldName lineTo script(javaName lineTo script(objectName lineTo script(anyName))),
				getName lineTo script(javaName lineTo script(objectName lineTo script(anyName)))
			)
		) {
			value(
				javaName fieldTo value(
					objectName fieldTo rhs(
						native(
							this
								.nativeValue(fieldName)
								.nativeValue(javaName)
								.nativeValue(objectName)
								.nativeObject
								.run { this as Field }
								.get(
									this
										.nativeValue(getName)
										.nativeValue(javaName)
										.nativeValue(objectName)
										.nativeObject
								)
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
				methodName fieldTo value(
					javaName fieldTo value(
						objectName fieldTo rhs(
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
											.toTypedArray()))))))
		}

val javaMethodInvokeDefinition
	get() =
		nativeDefinition(
			script(
				methodName lineTo script(javaName lineTo script(anyName)),
				invokeName lineTo script(
					javaName lineTo script(anyName),
					argsName lineTo script(javaName lineTo script(anyName))
				)
			)
		) {
			value(
				javaName fieldTo value(
					objectName fieldTo rhs(
						native(
							this
								.nativeValue(methodName)
								.nativeValue(javaName)
								.javaObject
								.run { this as Method }
								.invoke(
									this
										.nativeValue(invokeName)
										.nativeValue(javaName)
										.javaObject,
									*this
										.nativeValue(invokeName)
										.nativeValue(argsName)
										.nativeValue(javaName)
										.javaObject as Array<*>
								)
						)
					)
				)
			)
		}
