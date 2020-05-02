package leo16

import leo.java.lang.typeClassOrNull
import leo14.bigDecimal
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
		?: applyNativeClassType(field)
		?: applyNativeClassNameText(field)

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

fun Value.applyNativeClassType(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(nativeName) { rhs ->
			rhs.matchPrefix(className) { rhs ->
				rhs.matchWord { word ->
					word.typeClassOrNull?.nativeValue
				}
			}
		}
	}

fun Value.applyNativeClassNameText(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(nativeName) { rhs ->
			rhs.matchPrefix(className) { rhs ->
				rhs.matchPrefix(nameName) { rhs ->
					rhs.matchText { text ->
						text.loadClassOrNull?.nativeValue
					}
				}
			}
		}
	}
