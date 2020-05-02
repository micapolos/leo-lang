package leo16

import leo.base.ifOrNull
import leo.base.runIfNotNull
import leo13.onlyOrNull
import leo15.*

fun Compiled.apply(field: Field): Compiled =
	value.normalize(field) { set(this).applyNormalized(it) }

fun Compiled.applyNormalized(field: Field): Compiled =
	null
		?: applyValue(field) // keep first
		?: applyBinding(field)
		?: applyEvaluate(field)
		?: applyCompile(field)
		?: applyQuote(field)
		?: applyGiving(field)
		?: applyGive(field)
		?: applyMatch(field)
		?: applyLibrary(field)
		?: applyImport(field)
		?: applyLoad(field)
		?: resolve(field)

fun Compiled.applyValue(field: Field): Compiled? =
	scope.runIfNotNull(value.apply(field)) { compiled(it) }

fun Compiled.applyQuote(field: Field): Compiled? =
	value.matchEmpty {
		field.matchPrefix(quoteName) { rhs ->
			scope.compiled(rhs)
		}
	}

fun Compiled.applyEvaluate(field: Field): Compiled? =
	value.matchEmpty {
		field.matchPrefix(evaluateName) { rhs ->
			scope.library.evaluate(rhs.printScript)?.let { scope.compiled(it) }
		}
	}

fun Compiled.applyCompile(field: Field): Compiled? =
	value.matchEmpty {
		field.matchPrefix(compileName) { rhs ->
			scope.compiler.plus(rhs.printScript).compiled
		}
	}

fun Compiled.resolve(field: Field): Compiled =
	scope.compiled(scope.library.resolve(value.plus(field)))

fun Compiled.applyBinding(field: Field): Compiled? =
	scope.applyBinding(value.plus(field))?.emptyCompiled

fun Compiled.applyGiving(field: Field): Compiled? =
	value.matchEmpty {
		field.matchPrefix(givingName) { rhs ->
			updateValue { scope.library.function(rhs.printScript).field.value }
		}
	}

fun Compiled.applyGive(field: Field): Compiled? =
	field.matchPrefix(giveName) { rhs ->
		scope.runIfNotNull(value.fieldStack.onlyOrNull?.functionOrNull?.invoke(rhs)) { compiled(it) }
	}

fun Compiled.applyMatch(field: Field): Compiled? =
	field.matchPrefix(matchName) { rhs ->
		value.matchValueOrNull?.let { matchValue ->
			scope.compiler.plus(rhs.printScript).compiled.let { compiled ->
				ifOrNull(compiled.value.isEmpty) {
					compiled.scope.exportLibrary.apply(matchValue)?.let { matching ->
						scope.compiled(matching)
					}
				}
			}
		}
	}

fun Compiled.applyLibrary(field: Field): Compiled? =
	value.matchEmpty {
		field.matchPrefix(libraryName) { rhs ->
			scope.compiler.plus(rhs.printScript).compiled.let { compiled ->
				ifOrNull(compiled.value.isEmpty) {
					scope.compiled(compiled.scope.exportLibrary.field.value)
				}
			}
		}
	}

fun Compiled.applyImport(field: Field): Compiled? =
	field.matchPrefix(importName) { rhs ->
		rhs.fieldStack.onlyOrNull?.libraryOrNull?.let { scope.plus(it) }?.compiled(value)
	}

fun Compiled.applyLoad(field: Field): Compiled? =
	field.matchPrefix(loadName) { rhs ->
		rhs.printScript.sentenceStack.onlyOrNull?.library?.let { scope.plus(it) }?.compiled(value)
	}

