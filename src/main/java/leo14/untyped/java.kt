package leo14.untyped

import leo13.map
import leo13.reverse
import leo13.splitOrNull
import leo13.toList
import leo14.stringOrNull

data class Java(val obj: Any?)

fun java(obj: Any?) = Java(obj)

val Sequence.resolveJava: Program?
	get() =
		null
			?: resolveJavaClass
			?: resolveJavaString
			?: resolveJavaInt
			?: resolveJavaFloat
			?: resolveJavaDouble
			?: resolveJavaMethod
			?: resolveJavaInvoke

val Sequence.resolveJavaString: Program?
	get() =
		matchInfix("java", "string") { lhs, rhs ->
			rhs.matchEmpty {
				lhs.matchText { text ->
					program(anyValue(text))
				}
			}
		}

val Sequence.resolveJavaInt: Program?
	get() =
		matchInfix("java", "int") { lhs, rhs ->
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

val Sequence.resolveJavaFloat: Program?
	get() =
		matchInfix("java", "float") { lhs, rhs ->
			rhs.matchEmpty {
				lhs.matchNumber { number ->
					program(anyValue(number.bigDecimal.toFloat()))
				}
			}
		}

val Sequence.resolveJavaDouble: Program?
	get() =
		matchInfix("java", "double") { lhs, rhs ->
			rhs.matchEmpty {
				lhs.matchNumber { number ->
					program(anyValue(number.bigDecimal.toDouble()))
				}
			}
		}

val Sequence.resolveJavaClass: Program?
	get() =
		matchInfix("java", "class") { lhs, rhs ->
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

val Sequence.resolveJavaMethod: Program?
	get() =
		try {
			matchInfix("class", "method") { lhs, rhs ->
				lhs.matchAny { any ->
					any
						.run { this as? Class<*> }
						?.let { clazz ->
							rhs
								.valueStack
								.splitOrNull
								?.let { (args, name) ->
									name
										.literalOrNull
										?.stringOrNull
										?.let { name ->
											args
												.map { anyOrNull }
												.map { this as Class<*> }
												.reverse
												.toList()
												.toTypedArray()
												.let { args -> clazz.getMethod(name, *args) }
												?.run { program(anyValue(this)) }
										}
								}
						}
				}
			}
		} catch (e: Throwable) {
			null
		}

val Sequence.resolveJavaInvoke
	get() =
		matchInfix("invoke") { lhs, rhs ->
			lhs.matchAny { any ->
				rhs.matchText { text ->
					try {
						program(anyValue(any.javaClass.getMethod(text).invoke(any)))
					} catch (x: RuntimeException) {
						null
					}
				}
			}
		}
