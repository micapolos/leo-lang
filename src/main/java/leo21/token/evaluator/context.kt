package leo21.token.evaluator

import leo14.lambda.value.Scope
import leo14.lambda.value.emptyScope
import leo14.lambda.value.push
import leo21.evaluator.Evaluated
import leo21.evaluator.EvaluatedGiven
import leo21.evaluator.compiled
import leo21.evaluator.evaluated
import leo21.prim.Prim
import leo21.token.body.Bindings
import leo21.token.body.binding
import leo21.token.body.emptyBindings
import leo21.token.body.given
import leo21.token.body.plus
import leo21.token.body.resolve
import leo21.token.type.compiler.Lines
import leo21.token.type.compiler.emptyLines

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