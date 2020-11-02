package leo21.prim.eval

import leo14.lambda.Term
import leo14.lambda.evalTerm
import leo14.lambda.evaluate
import leo14.lambda.evaluator
import leo14.lambda.value
import leo21.prim.Prim

val Term<Prim>.evaluate: Term<Prim>
	get() =
		evaluator(Prim::apply)
			.evaluate(value)
			.evalTerm