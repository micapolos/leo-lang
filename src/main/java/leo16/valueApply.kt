package leo16

import leo.java.lang.typeClassOrNull
import leo14.bigDecimal
import leo14.untyped.typed.loadClass
import leo14.untyped.typed.loadClassOrNull
import leo15.*
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
		?: applyNativeClassField(field)
		?: applyNativeFieldStaticGet(field)
		?: applyNativeFieldGet(field)

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

fun Value.applyNativeFieldStaticGet(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(getName) { rhs ->
			rhs.matchPrefix(fieldName) { rhs ->
				rhs.matchNative { nativeField ->
					rhs.matchNative { native ->
						nullIfThrowsException {
							(nativeField as java.lang.reflect.Field).get(native).nativeValue
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
				rhs.matchNative { nativeObject ->
					nullIfThrowsException {
						(nativeField as java.lang.reflect.Field).get(nativeObject).nativeValue
					}
				}
			}
		}
	}
