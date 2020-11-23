package leo21.token.evaluator

import leo.base.updateIfNotNull
import leo13.fold
import leo13.reverse
import leo14.ScriptLine
import leo14.Scriptable
import leo14.lambda.value.Scope
import leo14.lambda.value.emptyScope
import leo14.lambda.value.push
import leo14.lineTo
import leo14.script
import leo21.compiled.Compiled
import leo21.evaluated.Evaluated
import leo21.evaluated.EvaluatedGiven
import leo21.evaluated.resolve
import leo21.prim.Prim
import leo21.token.body.Bindings
import leo21.definition.Definition
import leo21.definition.Definitions
import leo21.token.body.Module
import leo21.token.body.binding
import leo21.definition.bindingOrNull
import leo21.token.body.emptyBindings
import leo21.definition.emptyDefinitions
import leo21.token.body.given
import leo21.definition.lineOrNull
import leo21.token.body.resolve
import leo21.token.body.resolveOrNull
import leo21.definition.valueOrNull
import leo21.token.body.plus
import leo21.token.type.compiler.Lines
import leo21.token.type.compiler.emptyLines
import leo21.token.type.compiler.plus

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
	bindings.resolveOrNull(scope, evaluated) ?: evaluated.resolve

val Context.beginModule: Module
	get() =
		Module(bindings, lines, emptyDefinitions)

fun Context.plus(definitions: Definitions): Context =
	fold(definitions.definitionStack.reverse, Context::plus)

fun Context.plus(definition: Definition): Context =
	Context(
		bindings.updateIfNotNull(definition.bindingOrNull) { plus(it) },
		lines.updateIfNotNull(definition.lineOrNull) { plus(it) },
		scope.updateIfNotNull(definition.valueOrNull) { push(it) })