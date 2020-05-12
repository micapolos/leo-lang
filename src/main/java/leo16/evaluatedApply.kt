package leo16

import leo.base.ifOrNull
import leo.base.runIfNotNull
import leo13.first
import leo13.mapOrNull
import leo16.names.*

inline fun Evaluated.apply(word: String, evaluated: Evaluated, mode: Mode): Evaluated =
	when (mode) {
		Mode.EVALUATE -> apply(word, evaluated, isType = false)
		Mode.TYPE -> apply(word, evaluated, isType = true)
		Mode.QUOTE -> plusNormalized(word(evaluated.value))
		Mode.META -> plusNormalized(word(evaluated.value))
	}

inline fun Evaluated.apply(word: String, evaluated: Evaluated, isType: Boolean): Evaluated =
	if (evaluated.value.isEmpty) clearValue.applyNormalized(word, evaluated.scope.evaluated(value), isType)
	else applyNormalized(word, evaluated, isType)

inline fun Evaluated.applyNormalized(word: String, evaluated: Evaluated, isType: Boolean): Evaluated =
	null
		?: applyDictionary(word, evaluated)
		?: applyNormalized(word(evaluated.value), isType)

inline fun Evaluated.applyDictionary(word: String, evaluated: Evaluated): Evaluated? =
	value.matchEmpty {
		ifOrNull(word == _dictionary) {
			evaluated.value.matchEmpty {
				scope.evaluated(evaluated.scope.exportDictionary.field.value)
			}
		}
	}

inline fun Evaluated.apply(field: Field, mode: Mode): Evaluated =
	when (mode) {
		Mode.EVALUATE -> apply(field, isType = false)
		Mode.TYPE -> apply(field, isType = true)
		Mode.QUOTE -> plusNormalized(field)
		Mode.META -> plusNormalized(field)
	}

inline fun Evaluated.apply(field: Field, isType: Boolean): Evaluated =
	value.normalize(field) { set(this).applyNormalized(it, isType) }

inline fun Evaluated.applyNormalized(field: Field, isType: Boolean): Evaluated =
	applyNormalizedAndRead(field.read, isType)

inline fun Evaluated.applyNormalizedAndRead(field: Field, isType: Boolean): Evaluated =
	null
		?: applyValue(field) // keep first
		?: ifOrNull(!isType) { applyBinding(field) }
		?: applyEvaluate(field)
		?: applyCompile(field)
		?: applyQuote(field)
		?: applyGive(field)
		?: applyFunction(field)
		?: applyMatch(field)
		?: applyChoice(field)
		?: applyImport(field)
		?: applyExport(field)
		?: applyTest(field)
		?: applyLoad(field)
		?: resolve(field)

inline fun Evaluated.applyValue(field: Field): Evaluated? =
	scope.runIfNotNull(value.apply(field)) { evaluated(it) }

inline fun Evaluated.applyQuote(field: Field): Evaluated? =
	field.matchPrefix(_quote) { rhs ->
		scope.evaluated(value.plus(rhs))
	}

inline fun Evaluated.applyEvaluate(field: Field): Evaluated? =
	value.matchEmpty {
		field.matchPrefix(_evaluate) { rhs ->
			scope.dictionary.evaluate(rhs)?.let { scope.evaluated(it) }
		}
	}

inline fun Evaluated.applyCompile(field: Field): Evaluated? =
	value.matchEmpty {
		field.matchPrefix(_compile) { rhs ->
			scope.emptyEvaluator.plus(rhs).evaluated
		}
	}

inline fun Evaluated.resolve(field: Field): Evaluated =
	scope.dictionary.resolve(scope.evaluated(value.plus(field)))

inline fun Evaluated.applyBinding(field: Field): Evaluated? =
	scope.applyBinding(value.plus(field))?.emptyEvaluated

inline fun Evaluated.applyFunction(field: Field): Evaluated? =
	field.matchPrefix(_function) { rhs ->
		scope.dictionary.givesOrNull(rhs)?.field?.let { set(value.plus(it)) }
	}

inline fun Evaluated.applyGive(field: Field): Evaluated? =
	field.matchPrefix(_give) { rhs ->
		set(scope.dictionary.compiled(rhs).invoke(value.match))
	}

inline fun Evaluated.applyChoice(field: Field): Evaluated? =
	field.matchPrefix(_choice) { rhs ->
		rhs.fieldStack
			.mapOrNull { caseFieldOrNull }
			?.choice
			?.field
			?.value
			?.let { set(it) }
	}

inline fun Evaluated.applyImport(field: Field): Evaluated? =
	field.matchPrefix(_import) { rhs ->
		rhs.loadedDictionaryOrNull?.let { scope.import(it) }?.evaluated(value)
	}

inline fun Evaluated.applyExport(field: Field): Evaluated? =
	field.matchPrefix(_export) { rhs ->
		rhs.loadedDictionaryOrNull?.let { scope.export(it) }?.evaluated(value)
	}

inline fun Evaluated.applyLoad(field: Field): Evaluated? =
	value.matchEmpty {
		field.matchPrefix(_load) { rhs ->
			rhs.loadedOrNull?.let { set(it) }
		}
	}

inline fun Evaluated.applyMatch(field: Field): Evaluated? =
	value.matchFieldOrNull?.let { matchField ->
		field.matchPrefix(_match) { rhs ->
			rhs.fieldStack
				.first { it.selectWord == matchField.selectWord }
				?.sentenceOrNull
				?.let { caseSentence ->
					scope
						.plus(caseSentence.word.pattern.is_(matchField.value).definition)
						.evaluate(caseSentence.value)
				}
		}
	}


inline fun Evaluated.applyTest(field: Field): Evaluated? =
	value.matchEmpty {
		field.matchPrefix(_test) { rhs ->
			null
				?: applyTestGives(rhs)
				?: applyTestMatches(rhs)
				?: testSyntaxError(rhs)
		}
	}

inline fun Evaluated.applyTestGives(value: Value): Evaluated? =
	value.matchInfix(_gives) { lhs, rhs ->
		scope.emptyEvaluator.plus(lhs).evaluated.value.let { evaluatedLhs ->
			scope.emptyEvaluator.plus(rhs).evaluated.value.let { evaluatedRhs ->
				if (evaluatedLhs == evaluatedRhs) this
				else throw AssertionError(
					value(
						_test(
							_error(
								_it(lhs),
								_gave(evaluatedLhs),
								_should(_give(evaluatedRhs))))).printed.toString())
			}
		}
	}

inline fun Evaluated.applyTestMatches(value: Value): Evaluated? =
	value.matchInfix(_matches) { lhs, rhs ->
		scope.emptyEvaluator.plus(lhs).evaluated.value.let { evaluatedLhs ->
			scope.emptyEvaluator.plus(rhs).evaluated.value.let { evaluatedRhs ->
				evaluatedRhs.pattern.matchOrNull(evaluatedLhs).let { match ->
					if (match != null) this
					else throw AssertionError(
						value(
							_test(
								_error(
									_it(lhs),
									_giving(evaluatedLhs),
									_does(_not(_match(evaluatedRhs)))))).printed.toString())
				}
			}
		}
	}

inline fun testSyntaxError(value: Value): Evaluated =
	throw AssertionError(value(_test(_error(_syntax(value)))).toString())
