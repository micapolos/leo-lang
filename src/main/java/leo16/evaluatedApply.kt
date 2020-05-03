package leo16

import leo.base.ifOrNull
import leo.base.runIfNotNull
import leo13.onlyOrNull
import leo15.*

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
		?: applyMatch(field)
		?: applyDictionary(field)
		?: applyImport(field)
		?: applyLoad(field)
		?: resolve(field)

fun Evaluated.applyValue(field: Field): Evaluated? =
	scope.runIfNotNull(value.apply(field)) { evaluated(it) }

fun Evaluated.applyQuote(field: Field): Evaluated? =
	value.matchEmpty {
		field.matchPrefix(quoteName) { rhs ->
			scope.evaluated(rhs)
		}
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
			scope.evaluator.plus(rhs).compiled
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

fun Evaluated.applyMatch(field: Field): Evaluated? =
	field.matchPrefix(matchName) { rhs ->
		value.matchValueOrNull?.let { matchValue ->
			scope.evaluator.plus(rhs).compiled.let { compiled ->
				ifOrNull(compiled.value.isEmpty) {
					compiled.scope.exportDictionary.apply(matchValue)?.let { matching ->
						scope.evaluated(matching)
					}
				}
			}
		}
	}

fun Evaluated.applyDictionary(field: Field): Evaluated? =
	value.matchEmpty {
		field.matchPrefix(dictionaryName) { rhs ->
			emptyEvaluator.plus(rhs).compiled.let { compiled ->
				ifOrNull(compiled.value.isEmpty) {
					scope.evaluated(compiled.scope.exportDictionary.field.value)
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

