package leo21.evaluated

import leo14.lambda.value.emptyScope
import leo21.prim.Prim
import leo14.lambda.value.Scope as ValueScope
import leo21.compiled.Scope as CompiledScope

data class Context(
	val primScope: ValueScope<Prim>,
	val compiledScope: CompiledScope
)

val emptyContext = Context(emptyScope(), leo21.compiled.emptyScope)
