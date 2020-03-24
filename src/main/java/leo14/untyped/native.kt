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

val Sequence.resolveNative: Value?
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

val Sequence.resolveJavaString: Value?
	get() =
		matchInfix(nativeName, stringName) { lhs, rhs ->
			rhs.matchEmpty {
				lhs.matchText { text ->
					value(line(native(text)))
				}
			}
		}

val Sequence.resolveNativeInt: Value?
	get() =
		matchInfix(nativeName, intName) { lhs, rhs ->
			rhs.matchEmpty {
				lhs.matchNumber { number ->
					try {
						value(line(native(number.bigDecimal.intValueExact())))
					} catch (e: ArithmeticException) {
						null
					}
				}
			}
		}

val Sequence.resolveNativeFloat: Value?
	get() =
		matchInfix(nativeName, floatName) { lhs, rhs ->
			rhs.matchEmpty {
				lhs.matchNumber { number ->
					value(line(native(number.bigDecimal.toFloat())))
				}
			}
		}

val Sequence.resolveNativeDouble: Value?
	get() =
		matchInfix(nativeName, doubleName) { lhs, rhs ->
			rhs.matchEmpty {
				lhs.matchNumber { number ->
					value(line(native(number.bigDecimal.toDouble())))
				}
			}
		}

val Sequence.resolveNativeClass: Value?
	get() =
		matchInfix(nativeName, className) { lhs, rhs ->
			rhs.matchEmpty {
				lhs.matchText { text ->
					try {
						value(line(native(javaClass.classLoader.loadClass(text))))
					} catch (x: ClassNotFoundException) {
						null
					}
				}
			}
		}


val Sequence.resolveNativeInvoke
	get() =
		matchInfix(invokeName) { lhs, rhs ->
			lhs.matchNative { native ->
				rhs.lineStack.splitOrNull?.let { (args, name) ->
					name.literalOrNull?.stringOrNull?.let { name ->
						args.map { nativeOrNull!!.obj!! }.toList().toTypedArray().let { args ->
							args.map { it.javaClass.forInvoke }.toTypedArray().let { types ->
								value(
									line(
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
		matchInfix(nativeName, newName) { lhs, rhs ->
			lhs.matchText { name ->
				rhs.lineStack.map { nativeOrNull!!.obj!! }.toList().toTypedArray().let { args ->
					args.map { it.javaClass.forInvoke }.toTypedArray().let { types ->
						value(
							line(
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
		matchPostfix(textName) { lhs ->
			lhs.matchNative { native ->
				(native.obj as? String)?.let { string ->
					value(literal(string))
				}
			}
		}

val Sequence.resolveNativeNumber
	get() =
		matchPostfix(numberName) { lhs ->
			lhs.matchNative { native ->
				null
					?: (native.obj as? Byte)?.let { value(literal(it.toInt())) }
					?: (native.obj as? Short)?.let { value(literal(it.toInt())) }
					?: (native.obj as? Int)?.let { value(literal(it)) }
					?: (native.obj as? Long)?.let { value(literal(it)) }
					?: (native.obj as? Float)?.let { value(literal(it.toDouble())) }
					?: (native.obj as? Double)?.let { value(literal(it)) }
			}
		}

val Class<*>.forInvoke: Class<*>
	get() =
		when {
			this == java.lang.Byte::class.java -> java.lang.Byte.TYPE
			this == java.lang.Short::class.java -> java.lang.Short.TYPE
			this == java.lang.Integer::class.java -> Integer.TYPE
			this == java.lang.Long::class.java -> java.lang.Long.TYPE
			this == java.lang.Float::class.java -> java.lang.Float.TYPE
			this == java.lang.Double::class.java -> java.lang.Double.TYPE
			else -> this
		}