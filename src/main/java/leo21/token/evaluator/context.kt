package leo21.token.evaluator

import leo13.fold
import leo13.reverse
import leo14.lambda.value.Scope
import leo14.lambda.value.emptyScope
import leo14.lambda.value.push
import leo21.evaluator.Evaluated
import leo21.evaluator.EvaluatedGiven
import leo21.evaluator.compiled
import leo21.evaluator.evaluated
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
import leo22.dsl.*

data class Context(
	val bindings: Bindings,
	val lines: Lines,
	val scope: Scope<Prim>
)

val emptyContext = Context(emptyBindings, emptyLines, emptyScope())

fun Context.plus(given: EvaluatedGiven): Context =
	Context(
		bindings.plus(given.evaluated.type.given.binding),
		lines,
		scope.push(given.evaluated.value))

fun Context.resolve(evaluated: Evaluated): Evaluated =
	bindings.resolve(evaluated.compiled).evaluated

val Context.beginModule: Module
	get() =
		Module(bindings, lines, emptyDefinitions)

fun Context.plus(module: Module): Context =
	Context(
		bindings,
		module.lines,
		scope.fold(module.definitions.definitionStack.reverse) { push(it.value) })
