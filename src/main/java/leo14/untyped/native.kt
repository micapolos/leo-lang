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

val Sequence.resolveNative: Thunk?
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
			?: resolveNativeList

val Sequence.resolveJavaString: Thunk?
	get() =
		matchInfix(nativeName) { lhs, rhs ->
			rhs.matchPrefix(stringName) { rhs ->
				rhs.matchEmpty {
					lhs.matchText { text ->
						thunk(value(line(native(text))))
					}
				}
			}
		}

val Sequence.resolveNativeInt: Thunk?
	get() =
		matchInfix(nativeName) { lhs, rhs ->
			rhs.matchPrefix(intName) { rhs ->
				rhs.matchEmpty {
					lhs.matchNumber { number ->
						try {
							thunk(value(line(native(number.bigDecimal.intValueExact()))))
						} catch (e: ArithmeticException) {
							null
						}
					}
				}
			}
		}

val Sequence.resolveNativeFloat: Thunk?
	get() =
		matchInfix(nativeName) { lhs, rhs ->
			rhs.matchPrefix(floatName) { rhs ->
				rhs.matchEmpty {
					lhs.matchNumber { number ->
						thunk(value(line(native(number.bigDecimal.toFloat()))))
					}
				}
			}
		}

val Sequence.resolveNativeDouble: Thunk?
	get() =
		matchInfix(nativeName) { lhs, rhs ->
			rhs.matchPrefix(doubleName) { rhs ->
				rhs.matchEmpty {
					lhs.matchNumber { number ->
						thunk(value(line(native(number.bigDecimal.toDouble()))))
					}
				}
			}
		}

val Sequence.resolveNativeClass: Thunk?
	get() =
		matchInfix(nativeName) { lhs, rhs ->
			rhs.matchPrefix(className) { rhs ->
				rhs.matchEmpty {
					lhs.matchText { text ->
						try {
							thunk(value(line(native(javaClass.classLoader.loadClass(text)))))
						} catch (x: ClassNotFoundException) {
							null
						}
					}
				}
			}
		}


val Sequence.resolveNativeInvoke: Thunk?
	get() =
		matchInfix(invokeName) { lhs, rhs ->
			lhs.matchNative { native ->
				rhs.value.lineStack.splitOrNull?.let { (args, name) ->
					name.literalOrNull?.stringOrNull?.let { name ->
						args.map { nativeOrNull!!.obj!! }.toList().toTypedArray().let { args ->
							args.map { it.javaClass.forInvoke }.toTypedArray().let { types ->
								thunk(
									value(
										line(
											native(
												native.obj!!.javaClass
													.getMethod(name, *types)
													.invoke(native.obj, *args)))))
							}
						}
					}
				}
			}
		}

val Sequence.resolveNativeNew: Thunk?
	get() =
		matchInfix(nativeName) { lhs, rhs ->
			rhs.matchPrefix(newName) { rhs ->
				lhs.matchText { name ->
					rhs.value.lineStack.map { nativeOrNull!!.obj!! }.toList().toTypedArray().let { args ->
						args.map { it.javaClass.forInvoke }.toTypedArray().let { types ->
							thunk(value(
								line(
									native(
										javaClass
											.classLoader
											.loadClass(name)
											.getConstructor(*types)
											.newInstance(*args)))))
						}
					}
				}
			}
		}

val Sequence.resolveNativeText: Thunk?
	get() =
		matchPostfix(textName) { lhs ->
			lhs.matchNative { native ->
				(native.obj as? String)?.let { string ->
					thunk(value(literal(string)))
				}
			}
		}

val Sequence.resolveNativeNumber: Thunk?
	get() =
		matchPostfix(numberName) { lhs ->
			lhs.matchNative { native ->
				null
					?: (native.obj as? Byte)?.let { thunk(value(literal(it.toInt()))) }
					?: (native.obj as? Short)?.let { thunk(value(literal(it.toInt()))) }
					?: (native.obj as? Int)?.let { thunk(value(literal(it))) }
					?: (native.obj as? Long)?.let { thunk(value(literal(it))) }
					?: (native.obj as? Float)?.let { thunk(value(literal(it.toDouble()))) }
					?: (native.obj as? Double)?.let { thunk(value(literal(it))) }
			}
		}

val Sequence.resolveNativeList: Thunk?
	get() =
		matchPostfix(listName) { lhs ->
			lhs.matchNative { native ->
				(native.obj as? Array<Any>)?.let { array ->
					thunk(
						value(
							"list" lineTo thunk(
								value(
									*array
										.map { line(native(it)) }
										.toTypedArray()))))
				}
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