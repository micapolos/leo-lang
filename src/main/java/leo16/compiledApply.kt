package leo16

import leo.base.ifOrNull
import leo.base.runIfNotNull
import leo15.*

fun Compiled.apply(line: Line): Compiled =
	if (line.value.isEmpty) library.compiled(value()).applyNormalized(line.word.invoke(value))
	else applyNormalized(line)

fun Compiled.applyNormalized(line: Line): Compiled =
	null
		?: applyBinding(line)
		?: applyValue(line)
		?: applyEvaluate(line)
		?: applyCompile(line)
		?: applyScript(line)
		?: applyScope(line)
		?: applyGiving(line)
		?: applyGive(line)
		?: applyMatch(line)
		?: plus(line)

fun Compiled.applyValue(line: Line): Compiled? =
	library.runIfNotNull(value.apply(line)) { compiled(it) }

fun Compiled.applyScript(line: Line): Compiled? =
	value.matchEmpty {
		line.matchPrefix(scriptName) { rhs ->
			library.compiled(rhs)
		}
	}

fun Compiled.applyEvaluate(line: Line): Compiled? =
	value.matchEmpty {
		line.matchPrefix(evaluateName) { rhs ->
			library.scope.evaluate(rhs.script)?.let { library.compiled(it) }
		}
	}

fun Compiled.applyCompile(line: Line): Compiled? =
	value.matchEmpty {
		line.matchPrefix(compileName) { rhs ->
			library.compiler.plus(rhs.script).compiled
		}
	}

fun Compiled.applyScope(line: Line): Compiled? =
	library.scope.apply(value.plus(line))?.let { library.compiled(it) }

fun Compiled.applyBinding(line: Line): Compiled? =
	library.applyBinding(value.plus(line))?.emptyCompiled

fun Compiled.applyGiving(line: Line): Compiled? =
	value.matchEmpty {
		line.matchPrefix(givingName) { rhs ->
			updateValue { library.scope.function(rhs.script).value }
		}
	}

fun Compiled.applyGive(line: Line): Compiled? =
	line.matchPrefix(giveName) { rhs ->
		library.runIfNotNull(value.functionOrNull?.invoke(rhs)) { compiled(it) }
	}

fun Compiled.applyMatch(line: Line): Compiled? =
	line.matchPrefix(matchName) { rhs ->
		library.compiler.plus(rhs.script).compiled.let { compiled ->
			ifOrNull(compiled.value.isEmpty) {
				compiled.library.publicScope.apply(value)?.let { matching ->
					library.compiled(matching)
				}
			}
		}
	}
