package leo21.evaluator

import leo14.lambda.value.Scope
import leo14.lambda.value.emptyScope
import leo21.compiler.Bindings
import leo21.compiler.emptyBindings
import leo21.prim.Prim

data class Context(
	val scope: Scope<Prim>,
	val bindings: Bindings
)

val emptyContext = Context(emptyScope(), emptyBindings)
