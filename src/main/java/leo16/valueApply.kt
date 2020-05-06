package leo16

import leo.base.ifOrNull
import leo.base.orNull
import leo13.fold
import leo13.isEmpty
import leo13.linkOrNull
import leo14.leonardoScript
import leo15.*

fun Value.apply(field: Field): Value? =
	null
		?: applyThing(field)
		?: applyGet(field)
		?: applyGive(field)
		?: applyThis(field)
		?: applyNothing(field)
		?: applyComment(field)
		?: applyScript(field)
		?: applyFold(field)
		?: applyMatches(field)
		?: applyLeonardo(field)

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
		takingOrNull?.invoke(rhs)
	}

fun Value.applyThis(field: Field): Value? =
	field.matchPrefix(thisName) { rhs ->
		plus(rhs)
	}

fun Value.applyNothing(field: Field): Value? =
	matchEmpty {
		field.match(nothingName) { value() }
	}

fun Value.applyComment(field: Field): Value? =
	field.matchPrefix(commentName) { this }

fun Value.applyScript(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(scriptName) { rhs ->
			rhs.print
		}
	}

fun Value.applyMatches(field: Field): Value? =
	field.matchPrefix(matchesName) { rhs ->
		matches(rhs).field.value
	}

fun Value.applyFold(field: Field): Value? =
	matchList { list ->
		field.matchPrefix(foldName) { rhs ->
			rhs.split { lhs, field ->
				field.matchPrefix(stepName) { rhs ->
					rhs.matchFunction(value(toName(anyName()), itemName(anyName()))) { function ->
						lhs.matchPrefix(toName) { from ->
							from.fold(list) { field ->
								function.invoke(value(toName(this), itemName(field)))
							}
						}
					}
				}
			}
		}
	}

fun Value.applyLeonardo(field: Field): Value? =
	matchEmpty {
		field.match(leonardoName) {
			leonardoScript.asValue
		}
	}

