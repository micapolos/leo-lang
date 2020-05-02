package leo16

import leo.java.lang.typeClassOrNull
import leo13.array
import leo13.map
import leo13.mapOrNull
import leo14.bigDecimal
import leo14.untyped.typed.loadClass
import leo15.*
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import java.math.BigDecimal
import kotlin.minus
import kotlin.plus
import kotlin.times

fun Value.apply(field: Field): Value? =
	null
		?: applyThing(field)
		?: applyGet(field)
		?: applyGive(field)
		?: applyThis(field)
		?: applyNothing(field)
		?: applyTextPlusText(field)
		?: applyTextLength(field)
		?: applyNumberPlusNumber(field)
		?: applyNumberMinusNumber(field)
		?: applyNumberTimesNumber(field)
		?: applyTypeNativeClass(field)
		?: applyTextNameNativeClass(field)
		?: applyNullNative(field)
		?: applyTrueNative(field)
		?: applyFalseNative(field)
		?: applyNumberByteNative(field)
		?: applyNumberIntNative(field)
		?: applyNumberLongNative(field)
		?: applyNumberFloatNative(field)
		?: applyNumberDoubleNative(field)
		?: applyNativeClassField(field)
		?: applyNativeFieldGet(field)
		?: applyNativeClassConstructor(field)
		?: applyNativeConstructorInvoke(field)
		?: applyNativeClassMethod(field)
		?: applyNativeMethodInvoke(field)

fun Value.applyGet(field: Field): Value? =
	matchEmpty {
		field.sentenceOrNull?.let { sentence ->
			sentence.value.getOrNull(sentence.word)
		}
	}

fun Value.applyThing(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(thingName) { rhs ->
			rhs.thingOrNull
		}
	}

fun Value.applyGive(field: Field): Value? =
	field.matchPrefix(giveName) { rhs ->
		functionOrNull?.invoke(rhs)
	}

fun Value.applyThis(field: Field): Value? =
	field.matchPrefix(thisName) { rhs ->
		plus(rhs)
	}

fun Value.applyNothing(field: Field): Value? =
	matchEmpty {
		field.match(nothingName) { value() }
	}

fun Value.applyTextPlusText(field: Field): Value? =
	matchText { lhsString ->
		field.matchPrefix(plusName) { rhs ->
			rhs.matchText { rhsString ->
				lhsString.plus(rhsString).field.value
			}
		}
	}

fun Value.applyTextLength(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(lengthName) { rhs ->
			rhs.matchText { text ->
				text.length.bigDecimal.field.value
			}
		}
	}

fun Value.applyNumberPlusNumber(field: Field): Value? =
	applyNumberOpNumber(field, plusName, BigDecimal::plus)

fun Value.applyNumberMinusNumber(field: Field): Value? =
	applyNumberOpNumber(field, minusName, BigDecimal::minus)

fun Value.applyNumberTimesNumber(field: Field): Value? =
	applyNumberOpNumber(field, timesName, BigDecimal::times)

fun Value.applyNumberOpNumber(field: Field, word: String, fn: BigDecimal.(BigDecimal) -> BigDecimal): Value? =
	matchNumber { lhsNumber ->
		field.matchPrefix(word) { rhs ->
			rhs.matchNumber { rhsNumber ->
				lhsNumber.fn(rhsNumber).field.value
			}
		}
	}

fun Value.applyNullNative(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(nativeName) { rhs ->
			rhs.matchPrefix(nullName) { rhs ->
				rhs.matchEmpty {
					null.nativeValue
				}
			}
		}
	}

fun Value.applyTrueNative(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(nativeName) { rhs ->
			rhs.matchPrefix(trueName) { rhs ->
				rhs.matchEmpty {
					true.nativeValue
				}
			}
		}
	}

fun Value.applyFalseNative(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(nativeName) { rhs ->
			rhs.matchPrefix(falseName) { rhs ->
				rhs.matchEmpty {
					false.nativeValue
				}
			}
		}
	}

fun Value.applyNumberByteNative(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(nativeName) { rhs ->
			rhs.matchPrefix(byteName) { rhs ->
				rhs.matchNumber { number ->
					nullIfThrowsException {
						number.byteValueExact().nativeValue
					}
				}
			}
		}
	}

fun Value.applyNumberIntNative(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(nativeName) { rhs ->
			rhs.matchPrefix(intName) { rhs ->
				rhs.matchNumber { number ->
					nullIfThrowsException {
						number.intValueExact().nativeValue
					}
				}
			}
		}
	}

fun Value.applyNumberLongNative(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(nativeName) { rhs ->
			rhs.matchPrefix(longName) { rhs ->
				rhs.matchNumber { number ->
					nullIfThrowsException {
						number.longValueExact().nativeValue
					}
				}
			}
		}
	}

fun Value.applyNumberFloatNative(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(nativeName) { rhs ->
			rhs.matchPrefix(floatName) { rhs ->
				rhs.matchNumber { number ->
					nullIfThrowsException {
						number.toFloat().nativeValue
					}
				}
			}
		}
	}

fun Value.applyNumberDoubleNative(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(nativeName) { rhs ->
			rhs.matchPrefix(doubleName) { rhs ->
				rhs.matchNumber { number ->
					nullIfThrowsException {
						number.toDouble().nativeValue
					}
				}
			}
		}
	}

fun Value.applyTypeNativeClass(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(className) { rhs ->
			rhs.matchPrefix(nativeName) { rhs ->
				rhs.matchWord { word ->
					word.typeClassOrNull?.let { class_ ->
						className(class_.nativeField).value
					}
				}
			}
		}
	}

fun Value.applyTextNameNativeClass(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(className) { rhs ->
			rhs.matchPrefix(nativeName) { rhs ->
				rhs.matchPrefix(nameName) { rhs ->
					rhs.matchText { text ->
						nullIfThrowsException {
							className(text.loadClass.nativeField).value
						}
					}
				}
			}
		}
	}

fun Value.applyNativeClassField(field: Field): Value? =
	matchPrefix(className) { lhs ->
		lhs.matchNative { native ->
			(native as? Class<*>)?.let { class_ ->
				field.matchPrefix(fieldName) { rhs ->
					rhs.matchPrefix(nameName) { rhs ->
						rhs.matchText { name ->
							nullIfThrowsException {
								fieldName(class_.getField(name).nativeField).value
							}
						}
					}
				}
			}
		}
	}

fun Value.applyNativeFieldGet(field: Field): Value? =
	matchPrefix(fieldName) { rhs ->
		rhs.matchNative { nativeField ->
			field.matchPrefix(getName) { rhs ->
				rhs.matchPrefix(objectName) { rhs ->
					rhs.matchNative { nativeObject ->
						nullIfThrowsException {
							(nativeField as java.lang.reflect.Field).get(nativeObject).nativeValue
						}
					}
				}
			}
		}
	}

fun Value.applyNativeClassConstructor(field: Field): Value? =
	matchPrefix(className) { rhs ->
		rhs.matchNative { nativeClass ->
			field.matchPrefix(constructorName) { rhs ->
				rhs.matchPrefix(parameterName) { rhs ->
					rhs
						.listOrNull {
							matchPrefix(className) { rhs ->
								rhs.matchNative { it }
							}
						}
						?.mapOrNull { this as? Class<*> }
						?.array
						?.let { parameterClasses ->
							nullIfThrowsException {
								constructorName((nativeClass as Class<*>)
									.getConstructor(*parameterClasses).nativeField)
									.value
							}
						}
				}
			}
		}
	}

fun Value.applyNativeConstructorInvoke(field: Field): Value? =
	matchPrefix(constructorName) { rhs ->
		rhs.matchNative { nativeConstructor ->
			field.matchPrefix(invokeName) { rhs ->
				rhs.matchPrefix(parameterName) { rhs ->
					rhs
						.listOrNull { this }
						?.mapOrNull { matchNative { it } }
						?.array
						?.let { args ->
							nullIfThrowsException {
								(nativeConstructor as Constructor<*>)
									.newInstance(*args)
									.nativeValue
							}
						}
				}
			}
		}
	}

fun Value.applyNativeClassMethod(field: Field): Value? =
	matchPrefix(className) { rhs ->
		rhs.matchNative { nativeClass ->
			field.matchPrefix(methodName) { rhs ->
				rhs.matchInfix(parameterName) { lhs, parameter ->
					lhs.matchPrefix(nameName) { lhs ->
						lhs.matchText { name ->
							parameter
								.listOrNull {
									matchPrefix(className) { rhs ->
										rhs.matchNative { it }
									}
								}
								?.mapOrNull { this as? Class<*> }
								?.array
								?.let { parameterClasses ->
									nullIfThrowsException {
										methodName((nativeClass as Class<*>)
											.getMethod(name, *parameterClasses).nativeField)
											.value
									}
								}
						}
					}
				}
			}
		}
	}

fun Value.applyNativeMethodInvoke(field: Field): Value? =
	matchPrefix(methodName) { rhs ->
		rhs.matchNative { nativeMethod ->
			field.matchPrefix(invokeName) { rhs ->
				rhs.matchInfix(parameterName) { lhs, parameter ->
					lhs.matchPrefix(objectName) { rhs ->
						rhs.matchNative { native ->
							parameter
								.listOrNull { this }
								?.mapOrNull { matchNative { it } }
								?.array
								?.let { args ->
									nullIfThrowsException {
										(nativeMethod as Method)
											.invoke(native, *args)
											.nativeValue
									}
								}
						}
					}
				}
			}
		}
	}



