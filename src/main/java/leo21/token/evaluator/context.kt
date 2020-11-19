package leo21.token.evaluator

import leo13.fold
import leo13.reverse
import leo14.ScriptLine
import leo14.Scriptable
import leo14.anyReflectScriptLine
import leo14.lambda.value.Scope
import leo14.lambda.value.emptyScope
import leo14.lambda.value.push
import leo14.lineTo
import leo14.script
import leo21.compiled.Compiled
import leo21.evaluator.Evaluated
import leo21.evaluator.EvaluatedGiven
import leo21.evaluator.compiled
import leo21.prim.Prim
import leo21.token.body.Bindings
import leo21.token.body.Definition
import leo21.token.body.Module
import leo21.token.body.binding
import leo21.token.body.emptyBindings
import leo21.token.body.emptyDefinitions
import leo21.token.body.given
import leo21.token.body.plus
import leo21.token.body.resolve
import leo21.token.body.value
import leo21.token.type.compiler.Lines
import leo21.token.type.compiler.emptyLines

data class Context(
	val bindings: Bindings,
	val lines: Lines,
	val scope: Scope<Prim>
) : Scriptable() {
	override val reflectScriptLine: ScriptLine
		get() = "context" lineTo script(
			bindings.reflectScriptLine,
			lines.reflectScriptLine,
			scope.reflectScriptLine)
}

val emptyContext = Context(emptyBindings, emptyLines, emptyScope())

fun Context.plus(given: EvaluatedGiven): Context =
	Context(
		bindings.plus(given.evaluated.type.given.binding),
		lines,
		scope.push(given.evaluated.value))

fun Context.resolve(compiled: Compiled): Evaluated =
	scope.evaluated(bindings.resolve(compiled))

fun Context.resolve(evaluated: Evaluated): Evaluated =
	scope.evaluated(bindings.resolve(evaluated.compiled))

val Context.beginModule: Module
	get() =
		Module(bindings, lines, emptyDefinitions)

fun Context.plus(module: Module): Context =
	this
		.fold(module.definitions.definitionStack.reverse, Context::plus)
		.copy(lines = module.lines)

fun Context.plus(definition: Definition): Context =
	Context(
		bindings.plus(definition.binding),
		lines,
		scope.push(definition.value))