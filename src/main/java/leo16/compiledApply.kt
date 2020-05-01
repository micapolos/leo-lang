package leo16

import leo.base.runIfNotNull
import leo15.*

fun Compiled.apply(line: Line): Compiled =
	if (line.value.isEmpty) scope.compiled(value()).applyNormalized(line.word.invoke(value))
	else applyNormalized(line)

fun Compiled.applyNormalized(line: Line): Compiled =
	null
		?: applyDefinition(line)
		?: applyValue(line)
		?: applyEvaluate(line)
		?: applyCompile(line)
		?: applyScript(line)
		?: applyScope(line)
		?: applyGiving(line)
		?: applyGive(line)
		?: plus(line)

fun Compiled.applyValue(line: Line): Compiled? =
	scope.runIfNotNull(value.apply(line)) { compiled(it) }

fun Compiled.applyScript(line: Line): Compiled? =
	value.matchEmpty {
		line.matchPrefix(scriptName) { rhs ->
			scope.compiled(rhs)
		}
	}

fun Compiled.applyEvaluate(line: Line): Compiled? =
	value.matchEmpty {
		line.matchPrefix(evaluateName) { rhs ->
			scope.evaluate(rhs.script)?.let { scope.compiled(it) }
		}
	}

fun Compiled.applyCompile(line: Line): Compiled? =
	value.matchEmpty {
		line.matchPrefix(compileName) { rhs ->
			scope.compile(rhs.script)
		}
	}

fun Compiled.applyScope(line: Line): Compiled? =
	scope.apply(value.plus(line))?.let { scope.compiled(it) }

fun Compiled.applyDefinition(line: Line): Compiled? =
	scope.applyDefinition(value.plus(line))?.compiled(value())

fun Compiled.applyGiving(line: Line): Compiled? =
	value.matchEmpty {
		line.matchPrefix(givingName) { rhs ->
			updateValue { scope.function(rhs.script).value }
		}
	}

fun Compiled.applyGive(line: Line): Compiled? =
	line.matchPrefix(giveName) { rhs ->
		scope.runIfNotNull(value.functionOrNull?.invoke(rhs)) { compiled(it) }
	}
