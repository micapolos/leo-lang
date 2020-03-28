package leo14.untyped

import leo13.map
import leo13.reverse
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
			?: resolveNativeStaticInvoke
			?: resolveNativeText
			?: resolveNativeNumber
			?: resolveNativeGet
			?: resolveNativeStaticGet
			?: resolveNativeList

val Sequence.resolveJavaString: Thunk?
	get() =
		matchPostfix(nativeName, stringName) { lhs ->
			lhs.matchText { text ->
				thunk(value(line(native(text))))
			}
		}

val Sequence.resolveNativeInt: Thunk?
	get() =
		matchPostfix(nativeName, intName) { lhs ->
			lhs.matchNumber { number ->
				try {
					thunk(value(line(native(number.bigDecimal.intValueExact()))))
				} catch (e: ArithmeticException) {
					null
				}
			}
		}

val Sequence.resolveNativeFloat: Thunk?
	get() =
		matchPostfix(nativeName, floatName) { lhs ->
			lhs.matchNumber { number ->
				thunk(value(line(native(number.bigDecimal.toFloat()))))
			}
		}

val Sequence.resolveNativeDouble: Thunk?
	get() =
		matchPostfix(nativeName, doubleName) { lhs ->
			lhs.matchNumber { number ->
				thunk(value(line(native(number.bigDecimal.toDouble()))))
			}
		}

val Sequence.resolveNativeClass: Thunk?
	get() =
		matchPostfix(nativeName, className) { lhs ->
			lhs.matchText { text ->
				try {
					thunk(value(line(native(javaClass.classLoader.loadClass(text)))))
				} catch (x: ClassNotFoundException) {
					null
				}
			}
		}


val Sequence.resolveNativeInvoke: Thunk?
	get() =
		matchInfix(invokeName) { lhs, rhs ->
			lhs.matchNative { native ->
				rhs.value.lineStack.splitOrNull?.let { (args, name) ->
					name.literalOrNull?.stringOrNull?.let { name ->
						args.reverse.map { nativeOrNull!!.obj!! }.toList().toTypedArray().let { args ->
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

val Sequence.resolveNativeStaticInvoke: Thunk?
	get() =
		matchInfix(invokeName, staticName) { lhs, rhs ->
			lhs.matchNative { native ->
				rhs.value.lineStack.splitOrNull?.let { (args, name) ->
					name.literalOrNull?.stringOrNull?.let { name ->
						args.reverse.map { nativeOrNull!!.obj!! }.toList().toTypedArray().let { args ->
							args.map { it.javaClass.forInvoke }.toTypedArray().let { types ->
								thunk(
									value(
										line(
											native(
												(native.obj as? Class<*>)
													?.getMethod(name, *types)
													?.invoke(null, *args)))))
							}
						}
					}
				}
			}
		}

val Sequence.resolveNativeNew: Thunk?
	get() =
		matchInfix(newName) { lhs, rhs ->
			lhs.matchNative { native ->
				(native.obj as? Class<*>)?.let { class_ ->
					rhs.value.lineStack.reverse.map { nativeOrNull!!.obj!! }.toList().toTypedArray().let { args ->
						args.map { it.javaClass.forInvoke }.toTypedArray().let { types ->
							thunk(value(
								line(
									native(
										class_.getConstructor(*types).newInstance(*args)))))
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

val Sequence.resolveNativeGet: Thunk?
	get() =
		matchInfix(getName) { lhs, rhs ->
			lhs.matchNative { native ->
				rhs.matchText { name ->
					native.obj?.javaClass?.forInvoke?.let { class_ ->
						thunk(value(line(native(class_.getField(name).get(native.obj)))))
					}
				}
			}
		}

val Sequence.resolveNativeStaticGet: Thunk?
	get() =
		matchInfix(getName, staticName) { lhs, rhs ->
			lhs.matchNative { native ->
				rhs.matchText { name ->
					(native.obj as? Class<*>)?.let { class_ ->
						thunk(value(line(native(class_.getField(name).get(null)))))
					}
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