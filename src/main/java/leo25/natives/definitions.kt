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
		) { null.javaValue }

val trueObjectJavaDefinition
	get() =
		nativeDefinition(
			script(
				objectName lineTo script(trueName),
				javaName lineTo script()
			)
		) { true.javaValue }

val falseObjectJavaDefinition
	get() =
		nativeDefinition(
			script(
				objectName lineTo script(falseName),
				javaName lineTo script()
			)
		) { false.javaValue }

val javaObjectClassDefinition
	get() =
		nativeDefinition(
			script(
				javaName lineTo script(anyName),
				getName lineTo script(className lineTo script())
			)
		) {
			this
				.nativeValue(javaName)
				.javaObject!!
				.javaClass
				.javaValue
		}

val textObjectJavaDefinition
	get() =
		nativeDefinition(
			script(
				objectName lineTo script(textName lineTo script(anyName)),
				javaName lineTo script()
			)
		) {
			nativeValue(objectName).nativeValue(textName).nativeText.javaValue
		}

val javaObjectTextDefinition
	get() =
		nativeDefinition(
			script(
				javaName lineTo script(anyName),
				textName lineTo script()
			)
		) {
			value(field(literal(nativeValue(javaName).javaObject as String)))
		}

val numberObjectJavaDefinition
	get() =
		nativeDefinition(
			script(
				objectName lineTo script(numberName lineTo script(anyName)),
				javaName lineTo script()
			)
		) {
			nativeValue(objectName).nativeValue(numberName).nativeNumber.javaValue
		}

val javaObjectNumberDefinition
	get() =
		nativeDefinition(
			script(
				javaName lineTo script(anyName),
				numberName lineTo script()
			)
		) {
			value(field(literal(nativeValue(javaName).javaObject as Number)))
		}

val numberIntegerObjectJavaDefinition
	get() =
		nativeDefinition(
			script(
				objectName lineTo script(integerName lineTo script(numberName lineTo script(anyName))),
				javaName lineTo script()
			)
		) {
			this
				.nativeValue(objectName)
				.nativeValue(integerName)
				.nativeValue(numberName)
				.nativeNumber
				.bigDecimal
				.intValueExact()
				.javaValue
		}

val javaObjectIntegerNumberDefinition
	get() =
		nativeDefinition(
			script(
				integerName lineTo script(javaName lineTo script(anyName)),
				numberName lineTo script()
			)
		) {
			value(field(literal(nativeValue(integerName).nativeValue(javaName).javaObject as Int)))
		}

val arrayObjectJavaDefinition
	get() =
		nativeDefinition(
			script(
				objectName lineTo script(arrayName lineTo script(anyName)),
				javaName lineTo script()
			)
		) {
			nativeValue(objectName).nativeValue(arrayName).nativeArray.javaValue
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
			this
				.nativeValue(objectName)
				.nativeValue(className)
				.nativeValue(nameName)
				.nativeValue(textName)
				.nativeText
				.loadClass
				.javaValue
		}

val javaClassFieldDefinition
	get() =
		nativeDefinition(
			script(
				className lineTo script(
					javaName lineTo script(anyName)
				),
				fieldName lineTo script(
					nameName lineTo script(
						textName lineTo script(anyName)
					)
				)
			)
		) {
			value(
				fieldName fieldTo this
					.nativeValue(className)
					.nativeValue(javaName)
					.javaObject
					.run { this as Class<*> }
					.getField(
						this
							.nativeValue(fieldName)
							.nativeValue(nameName)
							.nativeValue(textName)
							.nativeText
					)
					.javaValue)
		}

val javaFieldGetDefinition
	get() =
		nativeDefinition(
			script(
				fieldName lineTo script(javaName lineTo script(anyName)),
				getName lineTo script(javaName lineTo script(anyName))
			)
		) {
			this
				.nativeValue(fieldName)
				.nativeValue(javaName)
				.javaObject
				.run { this as Field }
				.get(
					this
						.nativeValue(getName)
						.nativeValue(javaName)
						.javaObject
				)
				.javaValue
		}

val javaClassMethodNameTextDefinition
	get() =
		nativeDefinition(
			script(
				className lineTo script(
					javaName lineTo script(anyName)
				),
				methodName lineTo script(
					nameName lineTo script(
						textName lineTo script(anyName)
					),
					typesName lineTo script(
						javaName lineTo script(anyName)
					)
				)
			)
		) {
			value(
				methodName fieldTo this
					.nativeValue(className)
					.nativeValue(javaName)
					.javaObject
					.run { this as Class<*> }
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
							.javaObject
							.run { this as Array<*> }
							.toList()
							.run { this as List<Class<*>> }
							.toTypedArray())
					.javaValue)
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
