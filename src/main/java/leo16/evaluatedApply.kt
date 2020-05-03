package leo16

import leo.base.ifOrNull
import leo.base.notNullIf
import leo.base.runIfNotNull
import leo13.onlyOrNull
import leo15.*

fun Evaluated.apply(word: String, evaluated: Evaluated, mode: Mode): Evaluated =
	when (mode) {
		Mode.EVALUATE -> apply(word, evaluated)
		Mode.QUOTE -> plusNormalized(word(evaluated.value))
	}

fun Evaluated.apply(word: String, evaluated: Evaluated): Evaluated =
	if (evaluated.value.isEmpty) clearValue.applyNormalized(word, evaluated.scope.evaluated(value))
	else applyNormalized(word, evaluated)

fun Evaluated.applyNormalized(word: String, evaluated: Evaluated): Evaluated =
	null
		?: applyDictionary(word, evaluated)
		?: applyMatch(word, evaluated)
		?: applyNormalized(word(evaluated.value))

fun Evaluated.applyDictionary(word: String, evaluated: Evaluated): Evaluated? =
	value.matchEmpty {
		notNullIf(word == dictionaryName) {
			scope.evaluated(evaluated.scope.exportDictionary.field.value)
		}
	}

fun Evaluated.apply(field: Field, mode: Mode): Evaluated =
	when (mode) {
		Mode.EVALUATE -> apply(field)
		Mode.QUOTE -> plusNormalized(field)
	}

fun Evaluated.apply(field: Field): Evaluated =
	value.normalize(field) { set(this).applyNormalized(it) }

fun Evaluated.applyNormalized(field: Field): Evaluated =
	null
		?: applyValue(field) // keep first
		?: applyBinding(field)
		?: applyEvaluate(field)
		?: applyCompile(field)
		?: applyQuote(field)
		?: applyGiving(field)
		?: applyGive(field)
		?: applyImport(field)
		?: applyLoad(field)
		?: resolve(field)

fun Evaluated.applyValue(field: Field): Evaluated? =
	scope.runIfNotNull(value.apply(field)) { evaluated(it) }

fun Evaluated.applyQuote(field: Field): Evaluated? =
	field.matchPrefix(quoteName) { rhs ->
		scope.evaluated(value.plus(rhs))
	}

fun Evaluated.applyEvaluate(field: Field): Evaluated? =
	value.matchEmpty {
		field.matchPrefix(evaluateName) { rhs ->
			scope.dictionary.evaluate(rhs)?.let { scope.evaluated(it) }
		}
	}

fun Evaluated.applyCompile(field: Field): Evaluated? =
	value.matchEmpty {
		field.matchPrefix(compileName) { rhs ->
			scope.emptyEvaluator.plus(rhs).evaluated
		}
	}

fun Evaluated.resolve(field: Field): Evaluated =
	scope.evaluated(scope.dictionary.resolve(value.plus(field)))

fun Evaluated.applyBinding(field: Field): Evaluated? =
	scope.applyBinding(value.plus(field))?.emptyEvaluated

fun Evaluated.applyGiving(field: Field): Evaluated? =
	value.matchEmpty {
		field.matchPrefix(givingName) { rhs ->
			updateValue { scope.dictionary.function(rhs).field.value }
		}
	}

fun Evaluated.applyGive(field: Field): Evaluated? =
	field.matchPrefix(giveName) { rhs ->
		scope.runIfNotNull(value.fieldStack.onlyOrNull?.functionOrNull?.invoke(rhs)) { evaluated(it) }
	}

fun Evaluated.applyMatch(word: String, evaluated: Evaluated): Evaluated? =
	value.matchEmpty {
		ifOrNull(word == matchName) {
			evaluated.value.matchValueOrNull?.let { matchValue ->
				evaluated.scope.exportDictionary.apply(matchValue)?.let { matching ->
					scope.evaluated(matching)
				}
			}
		}
	}

fun Evaluated.applyImport(field: Field): Evaluated? =
	field.matchPrefix(importName) { rhs ->
		rhs.fieldStack.onlyOrNull?.dictionaryOrNull?.let { scope.import(it) }?.evaluated(value)
	}

fun Evaluated.applyLoad(field: Field): Evaluated? =
	value.matchEmpty {
		field.matchPrefix(loadName) { rhs ->
			rhs.pattern.loadedValueOrNull?.let { loadedValue ->
				set(loadedValue)
			}
		}
	}

