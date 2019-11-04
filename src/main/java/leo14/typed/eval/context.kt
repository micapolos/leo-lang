package leo14.typed.eval

import leo13.stack
import leo14.any
import leo14.lambda.term
import leo14.typed.Context

val evalContext = Context(stack(), { term(it) }, { term(it.any) })