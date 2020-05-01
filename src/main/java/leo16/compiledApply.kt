package leo16

import leo.base.ifOrNull
import leo.base.runIfNotNull
import leo15.*

fun Compiled.apply(line: Line): Compiled =
	if (line.value.isEmpty) scope.compiled(value()).applyNormalized(line.word.invoke(value))
	else applyNormalized(line)

fun Compiled.applyNormalized(line: Line): Compiled =
	null
		?: applyBinding(line)
		?: applyValue(line)
		?: applyEvaluate(line)
		?: applyCompile(line)
		?: applyQuote(line)
		?: applyScope(line)
		?: applyGiving(line)
		?: applyGive(line)
		?: applyMatch(line)
		?: applyLibrary(line)
		?: applyImport(line)
		?: plus(line)

fun Compiled.applyValue(line: Line): Compiled? =
	scope.runIfNotNull(value.apply(line)) { compiled(it) }

fun Compiled.applyQuote(line: Line): Compiled? =
	value.matchEmpty {
		line.matchPrefix(quoteName) { rhs ->
			scope.compiled(rhs)
		}
	}

fun Compiled.applyEvaluate(line: Line): Compiled? =
	value.matchEmpty {
		line.matchPrefix(evaluateName) { rhs ->
			scope.library.evaluate(rhs.script)?.let { scope.compiled(it) }
		}
	}

fun Compiled.applyCompile(line: Line): Compiled? =
	value.matchEmpty {
		line.matchPrefix(compileName) { rhs ->
			scope.compiler.plus(rhs.script).compiled
		}
	}

fun Compiled.applyScope(line: Line): Compiled? =
	scope.library.apply(value.plus(line))?.let { scope.compiled(it) }

fun Compiled.applyBinding(line: Line): Compiled? =
	scope.applyBinding(value.plus(line))?.emptyCompiled

fun Compiled.applyGiving(line: Line): Compiled? =
	value.matchEmpty {
		line.matchPrefix(givingName) { rhs ->
			updateValue { scope.library.function(rhs.script).value }
		}
	}

fun Compiled.applyGive(line: Line): Compiled? =
	line.matchPrefix(giveName) { rhs ->
		scope.runIfNotNull(value.functionOrNull?.invoke(rhs)) { compiled(it) }
	}

fun Compiled.applyMatch(line: Line): Compiled? =
	line.matchPrefix(matchName) { rhs ->
		value.matchValueOrNull?.let { matchValue ->
			scope.compiler.plus(rhs.script).compiled.let { compiled ->
				ifOrNull(compiled.value.isEmpty) {
					compiled.scope.exportLibrary.apply(matchValue)?.let { matching ->
						scope.compiled(matching)
					}
				}
			}
		}
	}

fun Compiled.applyLibrary(line: Line): Compiled? =
	value.matchEmpty {
		line.matchPrefix(libraryName) { rhs ->
			scope.compiler.plus(rhs.script).compiled.let { compiled ->
				ifOrNull(compiled.value.isEmpty) {
					scope.compiled(compiled.scope.exportLibrary.value)
				}
			}
		}
	}

fun Compiled.applyImport(line: Line): Compiled? =
	line.matchPrefix(importName) { rhs ->
		rhs.libraryOrNull?.let { scope.plus(it) }?.compiled(value)
	}
