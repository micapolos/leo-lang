package leo20.tokenizer

import leo.base.fold
import leo.base.notNullOrError
import leo13.reverse
import leo13.seq
import leo14.Script
import leo14.tokenStack
import leo20.emptyScope
import leo20.script
import leo20.value

val Script.eval: Script
	get() =
		EvaluatorTokenizer(Evaluator(null, Evaluated(emptyScope, value())))
			.run { this as Tokenizer }
			.fold(tokenStack.reverse.seq) {
				push(it).notNullOrError("$this.push($it)")
			}
			.run { this as EvaluatorTokenizer }
			.evaluator.evaluated.value.script