package leo16

import leo.base.ifOrNull
import leo.base.runIfNotNull
import leo13.mapOrNull
import leo15.*

fun Evaluated.apply(word: String, evaluated: Evaluated, mode: Mode): Evaluated =
	when (mode) {
		Mode.EVALUATE -> apply(word, evaluated)
		Mode.QUOTE -> plusNormalized(word(evaluated.value))
		Mode.META -> plusNormalized(word(evaluated.value))
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
		ifOrNull(word == dictionaryName) {
			evaluated.value.matchEmpty {
				scope.evaluated(evaluated.scope.exportDictionary.field.value)
			}
		}
	}

fun Evaluated.apply(field: Field, mode: Mode): Evaluated =
	when (mode) {
		Mode.EVALUATE -> apply(field)
		Mode.QUOTE -> plusNormalized(field)
		Mode.META -> plusNormalized(field)
	}

fun Evaluated.apply(field: Field): Evaluated =
	value.normalize(field) { set(this).applyNormalized(it) }

fun Evaluated.applyNormalized(field: Field): Evaluated =
	null
		?: applyValue(field) // keep second
		?: applyBinding(field)
		?: applyEvaluate(field)
		?: applyCompile(field)
		?: applyQuote(field)
		?: applyGiving(field)
		?: applyGive(field)
		?: applyChoice(field)
		?: applyImport(field)
		?: applyExport(field)
		?: applyTest(field)
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
	scope.dictionary.resolve(scope.evaluated(value.plus(field)))

fun Evaluated.applyBinding(field: Field): Evaluated? =
	scope.applyBinding(value.plus(field))?.emptyEvaluated

fun Evaluated.applyGiving(field: Field): Evaluated? =
	field.matchPrefix(givingName) { rhs ->
		updateValue { value.pattern.giving(scope.dictionary.function(rhs)).field.value }
	}

fun Evaluated.applyGive(field: Field): Evaluated? =
	field.matchPrefix(giveName) { rhs ->
		set(scope.dictionary.function(rhs).invoke(value))
	}

fun Evaluated.applyChoice(field: Field): Evaluated? =
	field.matchPrefix(choiceName) { rhs ->
		rhs.fieldStack
			.mapOrNull { caseFieldOrNull }
			?.choice
			?.field
			?.value
			?.let { set(it) }
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
		rhs.loadedDictionaryOrNull?.let { scope.import(it) }?.evaluated(value)
	}

fun Evaluated.applyExport(field: Field): Evaluated? =
	field.matchPrefix(exportName) { rhs ->
		rhs.loadedDictionaryOrNull?.let { scope.export(it) }?.evaluated(value)
	}

fun Evaluated.applyLoad(field: Field): Evaluated? =
	value.matchEmpty {
		field.matchPrefix(loadName) { rhs ->
			rhs.loadedOrNull?.let { set(it) }
		}
	}

fun Evaluated.applyTest(field: Field): Evaluated? =
	value.matchEmpty {
		field.matchPrefix(testName) { rhs ->
			null
				?: applyTestGives(rhs)
				?: applyTestMatches(rhs)
				?: testSyntaxError(rhs)
		}
	}

fun Evaluated.applyTestGives(value: Value): Evaluated? =
	value.matchInfix(givesName) { lhs, rhs ->
		scope.emptyEvaluator.plus(lhs).evaluated.value.let { evaluatedLhs ->
			scope.emptyEvaluator.plus(rhs).evaluated.value.let { evaluatedRhs ->
				if (evaluatedLhs == evaluatedRhs) this
				else throw AssertionError(
					value(
						testName(
							errorName(
								itName(lhs),
								gaveName(evaluatedLhs),
								shouldName(giveName(evaluatedRhs))))).toString())
			}
		}
	}

fun Evaluated.applyTestMatches(value: Value): Evaluated? =
	value.matchInfix(matchesName) { lhs, rhs ->
		scope.emptyEvaluator.plus(lhs).evaluated.value.let { evaluatedLhs ->
			scope.emptyEvaluator.plus(rhs).evaluated.value.let { evaluatedRhs ->
				if (evaluatedLhs.matches(evaluatedRhs.pattern)) this
				else throw AssertionError(
					value(
						testName(
							errorName(
								itName(lhs),
								givingName(evaluatedLhs),
								doesName(notName(matchName(evaluatedRhs)))))).toString())
			}
		}
	}

fun testSyntaxError(value: Value): Evaluated =
	throw AssertionError(value(testName(errorName(syntaxName(value)))).toString())
