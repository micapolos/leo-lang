package leo21.token.evaluator

import leo14.lambda.value.Scope
import leo21.evaluator.Evaluated
import leo21.evaluator.compiled
import leo21.evaluator.evaluated
import leo21.prim.Prim
import leo21.token.body.Bindings
import leo21.token.body.resolve
import leo21.token.type.compiler.Lines

data class Context(
	val bindings: Bindings,
	val lines: Lines,
	val scope: Scope<Prim>
)

fun Context.resolve(evaluated: Evaluated): Evaluated =
	bindings.resolve(evaluated.compiled).evaluated