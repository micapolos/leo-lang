package leo16

import leo.java.lang.typeClassOrNull
import leo13.array
import leo13.mapOrNull
import leo14.untyped.typed.loadClass
import leo15.*
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import java.math.BigDecimal

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
		?: applyIntPlusInt(field)
		?: applyIntMinusInt(field)
		?: applyIntTimesInt(field)
		?: applyTypeClass(field)
		?: applyTextNameClass(field)
		?: applyNullNative(field)
		?: applyTrueNative(field)
		?: applyFalseNative(field)
		?: applyNumberByte(field)
		?: applyNumberInt(field)
		?: applyNumberLong(field)
		?: applyNumberFloat(field)
		?: applyNumberDouble(field)
		?: applyClassField(field)
		?: applyFieldGet(field)
		?: applyObjectGetField(field)
		?: applyClassConstructor(field)
		?: applyConstructorInvoke(field)
		?: applyClassMethod(field)
		?: applyMethodInvoke(field)
		?: applyObjectMethodInvoke(field)

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
				text.length.toBigDecimal().field.value
			}
		}
	}

fun Value.applyIntPlusInt(field: Field): Value? =
	applyIntOpInt(field, plusName, Int::plus)

fun Value.applyIntMinusInt(field: Field): Value? =
	applyIntOpInt(field, minusName, Int::minus)

fun Value.applyIntTimesInt(field: Field): Value? =
	applyIntOpInt(field, timesName, Int::times)

fun Value.applyIntOpInt(field: Field, word: String, fn: Int.(Int) -> Int): Value? =
	matchInt { lhsInt ->
		field.matchPrefix(word) { rhs ->
			rhs.matchInt { rhsInt ->
				lhsInt.fn(rhsInt).field.value
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

fun Value.applyNumberByte(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(byteName) { rhs ->
			rhs.matchNumber { number ->
				nullIfThrowsException {
					byteName(number.byteValueExact().nativeField).value
				}
			}
		}
	}

fun Value.applyNumberInt(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(intName) { rhs ->
			rhs.matchNumber { number ->
				nullIfThrowsException {
					intName(number.intValueExact().nativeField).value
				}
			}
		}
	}

fun Value.applyNumberLong(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(longName) { rhs ->
			rhs.matchNumber { number ->
				nullIfThrowsException {
					longName(number.longValueExact().nativeField).value
				}
			}
		}
	}

fun Value.applyNumberFloat(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(floatName) { rhs ->
			rhs.matchNumber { number ->
				nullIfThrowsException {
					floatName(number.toFloat().nativeField).value
				}
			}
		}
	}

fun Value.applyNumberDouble(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(doubleName) { rhs ->
			rhs.matchNumber { number ->
				nullIfThrowsException {
					doubleName(number.toDouble().nativeField).value
				}
			}
		}
	}

fun Value.applyTypeClass(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(className) { rhs ->
			rhs.matchWord { word ->
				word.typeClassOrNull?.let { class_ ->
					className(class_.nativeField).value
				}
			}
		}
	}

fun Value.applyTextNameClass(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(className) { rhs ->
			rhs.matchPrefix(nameName) { rhs ->
				rhs.matchText { text ->
					nullIfThrowsException {
						className(text.loadClass.nativeField).value
					}
				}
			}
		}
	}

fun Value.applyClassField(field: Field): Value? =
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

fun Value.applyFieldGet(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(getName) { rhs ->
			rhs.matchPrefix(fieldName) { rhs ->
				rhs.matchNative { nativeField ->
					nullIfThrowsException {
						(nativeField as java.lang.reflect.Field).get(null).nativeValue
					}
				}
			}
		}
	}

fun Value.applyObjectGetField(field: Field): Value? =
	matchNative { nativeObject ->
		field.matchPrefix(getName) { rhs ->
			rhs.matchPrefix(fieldName) { rhs ->
				rhs.matchNative { nativeField ->
					nullIfThrowsException {
						(nativeField as java.lang.reflect.Field).get(nativeObject!!).nativeValue
					}
				}
			}
		}
	}

fun Value.applyClassConstructor(field: Field): Value? =
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

fun Value.applyConstructorInvoke(field: Field): Value? =
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

fun Value.applyClassMethod(field: Field): Value? =
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

fun Value.applyMethodInvoke(field: Field): Value? =
	matchPrefix(methodName) { method ->
		method.matchNative { nativeMethod ->
			field.matchPrefix(invokeName) { lhs ->
				lhs.matchPrefix(parameterName) { parameter ->
					parameter
						.listOrNull { this }
						?.mapOrNull { matchNative { it } }
						?.array
						?.let { args ->
							nullIfThrowsException {
								(nativeMethod as Method)
									.invoke(null, *args)
									.nativeValue
							}
						}
				}
			}
		}
	}

fun Value.applyObjectMethodInvoke(field: Field): Value? =
	matchNative { native ->
		field.matchPrefix(invokeName) { rhs ->
			rhs.matchInfix(parameterName) { lhs, parameter ->
				lhs.matchPrefix(methodName) { method ->
					method.matchNative { nativeMethod ->
						parameter
							.listOrNull { this }
							?.mapOrNull { matchNative { it } }
							?.array
							?.let { args ->
								nullIfThrowsException {
									(nativeMethod as Method)
										.invoke(native!!, *args)
										.nativeValue
								}
							}
					}
				}
			}
		}
	}


