package leo16

import leo.base.runIfNotNull
import leo15.compileName
import leo15.evaluateName
import leo15.givingName
import leo15.scriptName

val Compiled.apply: Compiled
	get() =
		updateValue { normalize }.applyNormalized

val Compiled.applyNormalized: Compiled
	get() =
		null
			?: applyDefinition
			?: applyValue
			?: applyEvaluate
			?: applyCompile
			?: applyScript
			?: applyScope
			?: applyGiving
			?: this

val Compiled.applyValue: Compiled?
	get() =
		scope.runIfNotNull(value.apply) { compiled(it) }

val Compiled.applyScript: Compiled?
	get() =
		value.matchPrefix(scriptName) { rhs ->
			scope.compiled(rhs)
		}

val Compiled.applyEvaluate: Compiled?
	get() =
		value.matchPrefix(evaluateName) { rhs ->
			scope.evaluate(rhs.script)?.let { scope.compiled(it) }
		}

val Compiled.applyCompile: Compiled?
	get() =
		value.matchPrefix(compileName) { rhs ->
			scope.compile(rhs.script)
		}

val Compiled.applyScope: Compiled?
	get() =
		scope.apply(value)?.let { scope.compiled(it) }

val Compiled.applyDefinition: Compiled?
	get() =
		scope.applyDefinition(value)?.compiled(value())

val Compiled.applyGiving: Compiled?
	get() =
		value.matchPrefix(givingName) { rhs ->
			updateValue { scope.function(rhs.script).value }
		}
