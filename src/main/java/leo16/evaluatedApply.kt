package leo16

import leo.base.ifOrNull
import leo.base.orIfNull
import leo.base.println
import leo.base.runIfNotNull
import leo13.filterNulls
import leo13.first
import leo13.map
import leo13.reverse
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
	applyNormalized(word(evaluated.value), isType)

fun Evaluated.apply(field: Sentence, mode: Mode): Evaluated =
	when (mode) {
		Mode.EVALUATE -> apply(field, isType = false)
		Mode.TYPE -> apply(field, isType = true)
		Mode.QUOTE -> plusNormalized(field)
		Mode.META -> plusNormalized(field)
	}

fun Evaluated.apply(field: Sentence, isType: Boolean): Evaluated =
	value.normalize(field) { set(this).applyNormalized(it, isType) }

inline fun Evaluated.applyNormalized(field: Sentence, isType: Boolean): Evaluated =
	applyNormalizedAndRead(scope.dictionary.applyRead(field), isType)

inline fun Evaluated.applyNormalizedAndRead(field: Sentence, isType: Boolean): Evaluated =
	null
		?: applyValue(field) // keep first
		?: ifOrNull(!isType) { applyBinding(field) }
		?: applyApply(field)
		?: applyEvaluate(field)
		?: applyCompile(field)
		?: applyQuote(field)
		?: applyMeta(field)
		?: applyDo(field)
		?: applyLazy(field)
		?: applyFunction(field)
		?: applyMatch(field)
		?: applyExport(field)
		?: applyUse(field)
		?: applyTest(field)
		?: applyDefinitionList(field)
		?: resolve(field)

fun Evaluated.applyValue(field: Sentence): Evaluated? =
	scope.runIfNotNull(value.apply(field)) { evaluated(it) }

fun Evaluated.applyQuote(field: Sentence): Evaluated? =
	value.matchEmpty {
		field.matchPrefix(_quote) { rhs ->
			scope.evaluated(rhs)
		}
	}

fun Evaluated.applyMeta(field: Sentence): Evaluated? =
	field.matchPrefix(_meta) { rhs ->
		rhs.onlySentenceOrNull?.let { sentence ->
			scope.evaluated(value.plus(sentence))
		}
	}

fun Evaluated.applyApply(field: Sentence): Evaluated? =
	value.matchEmpty {
		field.matchPrefix(_apply) { rhs ->
			scope.dictionary.apply(rhs.evaluated)?.let { scope.evaluated(it.value) }
		}
	}

fun Evaluated.applyEvaluate(field: Sentence): Evaluated? =
	value.matchEmpty {
		field.matchPrefix(_evaluate) { rhs ->
			scope.dictionary.evaluate(rhs).let { scope.evaluated(it) }
		}
	}

fun Evaluated.applyCompile(field: Sentence): Evaluated? =
	value.matchEmpty {
		field.matchPrefix(_compile) { rhs ->
			scope.emptyEvaluator.plus(rhs).evaluated
		}
	}

fun Evaluated.resolve(sentence: Sentence): Evaluated =
	scope.dictionary.resolve(scope.evaluated(value.plus(sentence)))

fun Evaluated.applyBinding(field: Sentence): Evaluated? =
	scope.applyBinding(value.plus(field))?.emptyEvaluated

fun Evaluated.applyFunction(field: Sentence): Evaluated? =
	value.matchEmpty {
		field.matchPrefix(_function) { rhs ->
			scope.dictionary.doesOrNull(rhs)?.value?.let { set(it) }
		}
	}

fun Evaluated.applyDo(field: Sentence): Evaluated? =
	field.matchPrefix(_do) { rhs ->
		set(scope.dictionary.compiled(rhs).invoke(value))
	}

fun Evaluated.applyUse(field: Sentence): Evaluated? =
	field.matchPrefix(_use) { rhs ->
		scope.dictionary.compile(rhs).let { rhs ->
			rhs.value.loadedOrNull
				.orIfNull { rhs }
				.let { rhs -> scope.useOrNull(rhs)?.evaluated(value) }
		}
	}

fun Evaluated.applyLazy(field: Sentence): Evaluated? =
	value.matchEmpty {
		field.matchPrefix(_lazy) { rhs ->
			set(scope.dictionary.compiled(rhs).lazy.value)
		}
	}

fun Evaluated.applyExport(field: Sentence): Evaluated? =
	field.matchPrefix(_export) { rhs ->
		rhs.loadedDictionaryOrNull?.let { scope.export(it) }?.evaluated(value)
	}

fun Evaluated.applyMatch(field: Sentence): Evaluated? =
	field.matchPrefix(_match) { rhs ->
		value.thingOrNull?.let { matchedValue ->
			matchedValue.matchWordOrNull?.let { matchWord ->
				rhs.sentenceStackOrNull
					?.first { it.word == matchWord }
					?.let { caseSentence ->
						scope
							.plus(value(matchWord()).is_(matchedValue).definition)
							.evaluate(caseSentence.rhsValue)
					}
			}
		}
	}

fun Evaluated.applyDefinitionList(field: Sentence): Evaluated? =
	field.matchPrefix(_list) { rhs ->
		rhs.match(_definition) {
			set(
				scope.dictionary.definitionStack.reverse
					.map { patternValueOrNull }
					.filterNulls
					.valueField
					.rhsValue)
		}
	}

fun Evaluated.applyTest(field: Sentence): Evaluated? =
	value.matchEmpty {
		field.matchPrefix(_test) { rhs ->
			null
				?: applyTestEquals(rhs)
				?: applyTestMatches(rhs)
				?: testSyntaxError(rhs)
		}
	}

fun Evaluated.applyTestEquals(value: Value): Evaluated? =
	value.matchInfix(_equals) { lhs, rhs ->
		scope.emptyEvaluator.plus(lhs).evaluated.value.let { evaluatedLhs ->
			scope.emptyEvaluator.plus(rhs).evaluated.value.let { evaluatedRhs ->
				if (evaluatedLhs == evaluatedRhs) this
				else throw AssertionError(
					value(
						_test(
							_error(
								_it(lhs),
								_equals(evaluatedLhs),
								_should(_equal(evaluatedRhs))))).printed.toString())
			}
		}
	}

fun Evaluated.applyTestMatches(value: Value): Evaluated? =
	value.matchInfix(_matches) { lhs, rhs ->
		scope.emptyEvaluator.plus(lhs).evaluated.value.let { evaluatedLhs ->
			scope.emptyEvaluator.plus(rhs).evaluated.value.let { evaluatedRhs ->
				if (evaluatedLhs.matches(evaluatedRhs)) this
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

fun testSyntaxError(value: Value): Evaluated =
	throw AssertionError(value(_test(_error(_syntax(value)))).toString())
