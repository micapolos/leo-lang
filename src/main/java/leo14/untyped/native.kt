package leo14.untyped

import leo13.map
import leo13.splitOrNull
import leo13.toList
import leo14.literal
import leo14.stringOrNull

data class Native(val obj: Any?) {
	override fun toString() = obj.toString()
}

fun native(obj: Any?) = Native(obj)

val Sequence.resolveNative: Program?
	get() =
		null
			?: resolveNativeClass
			?: resolveNativeNew
			?: resolveJavaString
			?: resolveNativeInt
			?: resolveNativeFloat
			?: resolveNativeDouble
			?: resolveNativeInvoke
			?: resolveNativeText
			?: resolveNativeNumber

val Sequence.resolveJavaString: Program?
	get() =
		matchInfix("native", "string") { lhs, rhs ->
			rhs.matchEmpty {
				lhs.matchText { text ->
					program(value(native(text)))
				}
			}
		}

val Sequence.resolveNativeInt: Program?
	get() =
		matchInfix("native", "int") { lhs, rhs ->
			rhs.matchEmpty {
				lhs.matchNumber { number ->
					try {
						program(value(native(number.bigDecimal.intValueExact())))
					} catch (e: ArithmeticException) {
						null
					}
				}
			}
		}

val Sequence.resolveNativeFloat: Program?
	get() =
		matchInfix("native", "float") { lhs, rhs ->
			rhs.matchEmpty {
				lhs.matchNumber { number ->
					program(value(native(number.bigDecimal.toFloat())))
				}
			}
		}

val Sequence.resolveNativeDouble: Program?
	get() =
		matchInfix("native", "double") { lhs, rhs ->
			rhs.matchEmpty {
				lhs.matchNumber { number ->
					program(value(native(number.bigDecimal.toDouble())))
				}
			}
		}

val Sequence.resolveNativeClass: Program?
	get() =
		matchInfix("native", "class") { lhs, rhs ->
			rhs.matchEmpty {
				lhs.matchText { text ->
					try {
						program(value(native(javaClass.classLoader.loadClass(text))))
					} catch (x: ClassNotFoundException) {
						null
					}
				}
			}
		}


val Sequence.resolveNativeInvoke
	get() =
		matchInfix("invoke") { lhs, rhs ->
			lhs.matchNative { native ->
				rhs.valueStack.splitOrNull?.let { (args, name) ->
					name.literalOrNull?.stringOrNull?.let { name ->
						args.map { nativeOrNull!!.obj!! }.toList().toTypedArray().let { args ->
							args.map { it.javaClass.forInvoke }.toTypedArray().let { types ->
								program(
									value(
										native(
											native.obj!!.javaClass
												.getMethod(name, *types)
												.invoke(native.obj, *args))))
							}
						}
					}
				}
			}
		}

val Sequence.resolveNativeNew
	get() =
		matchInfix("native", "new") { lhs, rhs ->
			lhs.matchText { name ->
				rhs.valueStack.map { nativeOrNull!!.obj!! }.toList().toTypedArray().let { args ->
					args.map { it.javaClass.forInvoke }.toTypedArray().let { types ->
						program(
							value(
								native(
									javaClass
										.classLoader
										.loadClass(name)
										.getConstructor(*types)
										.newInstance(*args))))
					}
				}
			}
		}

val Sequence.resolveNativeText
	get() =
		matchPostfix("text") { lhs ->
			lhs.matchNative { native ->
				(native.obj as? String)?.let { string ->
					program(literal(string))
				}
			}
		}

val Sequence.resolveNativeNumber
	get() =
		matchPostfix("number") { lhs ->
			lhs.matchNative { native ->
				null
					?: (native.obj as? Byte)?.let { program(literal(it.toInt())) }
					?: (native.obj as? Short)?.let { program(literal(it.toInt())) }
					?: (native.obj as? Int)?.let { program(literal(it)) }
					?: (native.obj as? Long)?.let { program(literal(it)) }
					?: (native.obj as? Float)?.let { program(literal(it.toDouble())) }
					?: (native.obj as? Double)?.let { program(literal(it)) }
			}
		}

val Class<*>.forInvoke: Class<*>
	get() =
		when {
			this == java.lang.Byte::javaClass -> java.lang.Byte.TYPE
			this == java.lang.Short::javaClass -> java.lang.Short.TYPE
			this == java.lang.Integer::javaClass -> Integer.TYPE
			this == java.lang.Long::javaClass -> java.lang.Long.TYPE
			this == java.lang.Float::javaClass -> java.lang.Float.TYPE
			this == java.lang.Double::javaClass -> java.lang.Double.TYPE
			else -> this
		}