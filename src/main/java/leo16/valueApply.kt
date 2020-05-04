package leo16

import leo15.*

fun Value.apply(field: Field): Value? =
	null
		?: applyThing(field)
		?: applyGet(field)
		?: applyGive(field)
		?: applyThis(field)
		?: applyNothing(field)
		?: applyNullNative(field)
		?: applyTrueNative(field)
		?: applyFalseNative(field)
		?: applyNumberByte(field)
		?: applyNumberInt(field)
		?: applyNumberLong(field)
		?: applyNumberFloat(field)
		?: applyNumberDouble(field)
		?: applyComment(field)
		?: applyScript(field)

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

fun Value.applyComment(field: Field): Value? =
	field.matchPrefix(commentName) { this }

fun Value.applyScript(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(scriptName) { rhs ->
			rhs.print
		}
	}
