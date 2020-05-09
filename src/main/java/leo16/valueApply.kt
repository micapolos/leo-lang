package leo16

import leo13.fold
import leo14.leonardoScript
import leo15.commentName
import leo15.leonardoName
import leo15.matchesName
import leo15.nothingName
import leo15.scriptName
import leo15.takeName
import leo15.thingName
import leo15.thisName
import leo16.names.*

fun Value.apply(field: Field): Value? =
	null
		?: applyThing(field)
		?: applyGet(field)
		?: applyTake(field)
		?: applyThis(field)
		?: applyNothing(field)
		?: applyComment(field)
		?: applyScript(field)
		?: applyStackFold(field)
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

fun Value.applyTake(field: Field): Value? =
	thingOrNull?.run {
		field.matchPrefix(takeName) { rhs ->
			takingOrNull?.take(rhs)
		}
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
			rhs.printed
		}
	}

fun Value.applyMatches(field: Field): Value? =
	field.matchPrefix(matchesName) { rhs ->
		matches(rhs).field.value
	}

fun Value.applyStackFold(field: Field): Value? =
	matchStack { stack ->
		field.matchPrefix(_fold) { rhs ->
			rhs.split { lhs, field ->
				field.matchPrefix(_step) { rhs ->
					rhs.matchFunction(value(_to(_any()), _item(_any()))) { function ->
						lhs.matchPrefix(_to) { from ->
							from.fold(stack) { value ->
								function.invoke(value(_to(this), _item(value)))
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

