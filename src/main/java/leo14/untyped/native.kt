package leo14.untyped

import leo13.map
import leo13.splitOrNull
import leo13.toList
import leo14.literal
import leo14.stringOrNull

data class Native(val obj: Any?)

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
					program(anyValue(text))
				}
			}
		}

val Sequence.resolveNativeInt: Program?
	get() =
		matchInfix("native", "int") { lhs, rhs ->
			rhs.matchEmpty {
				lhs.matchNumber { number ->
					try {
						program(anyValue(number.bigDecimal.intValueExact()))
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
					program(anyValue(number.bigDecimal.toFloat()))
				}
			}
		}

val Sequence.resolveNativeDouble: Program?
	get() =
		matchInfix("native", "double") { lhs, rhs ->
			rhs.matchEmpty {
				lhs.matchNumber { number ->
					program(anyValue(number.bigDecimal.toDouble()))
				}
			}
		}

val Sequence.resolveNativeClass: Program?
	get() =
		matchInfix("native", "class") { lhs, rhs ->
			rhs.matchEmpty {
				lhs.matchText { text ->
					try {
						program(anyValue(javaClass.classLoader.loadClass(text)))
					} catch (x: ClassNotFoundException) {
						null
					}
				}
			}
		}


val Sequence.resolveNativeInvoke
	get() =
		try {
			matchInfix("invoke") { lhs, rhs ->
				lhs.matchNative { any ->
					rhs.valueStack.splitOrNull?.let { (args, name) ->
						name.literalOrNull?.stringOrNull?.let { name ->
							args.map { anyOrNull!! }.toList().toTypedArray().let { args ->
								args.map { it.javaClass }.toTypedArray().let { types ->
									program(
										anyValue(
											any.javaClass
												.getMethod(name, *types)
												.invoke(any, *args)))
								}
							}
						}
					}
				}
			}
		} catch (t: Throwable) {
			null
		}

val Sequence.resolveNativeNew
	get() =
		try {
			matchInfix("native", "new") { lhs, rhs ->
				lhs.matchText { name ->
					rhs.valueStack.map { anyOrNull!! }.toList().toTypedArray().let { args ->
						args.map { it.javaClass }.toTypedArray().let { types ->
							program(
								anyValue(
									javaClass
										.classLoader
										.loadClass(name)
										.getConstructor(*types)
										.newInstance(*args)))
						}
					}
				}
			}
		} catch (t: Throwable) {
			null
		}

val Sequence.resolveNativeText
	get() =
		matchPostfix("text") { lhs ->
			lhs.matchNative { native ->
				(native as? String)?.let { string ->
					program(literal(string))
				}
			}
		}

val Sequence.resolveNativeNumber
	get() =
		matchPostfix("number") { lhs ->
			lhs.matchNative { any ->
				null
					?: (any as? Byte)?.let { program(literal(it.toInt())) }
					?: (any as? Short)?.let { program(literal(it.toInt())) }
					?: (any as? Int)?.let { program(literal(it)) }
					?: (any as? Long)?.let { program(literal(it)) }
					?: (any as? Float)?.let { program(literal(it.toDouble())) }
					?: (any as? Double)?.let { program(literal(it)) }
			}
		}

