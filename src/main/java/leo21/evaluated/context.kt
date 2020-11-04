package leo21.evaluated

import leo14.lambda.value.Scope
import leo14.lambda.value.emptyScope
import leo21.compiled.Bindings
import leo21.compiled.emptyBindings
import leo21.prim.Prim

data class Context(
	val scope: Scope<Prim>,
	val bindings: Bindings
)

val emptyContext = Context(emptyScope(), emptyBindings)
