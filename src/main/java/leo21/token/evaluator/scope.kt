package leo21.token.evaluator

import leo14.lambda.value.Scope
import leo21.compiled.Compiled
import leo21.evaluator.Evaluated
import leo21.evaluator.of
import leo21.prim.Prim
import leo21.value.value

fun Scope<Prim>.evaluated(compiled: Compiled): Evaluated =
	value(compiled.term) of compiled.type