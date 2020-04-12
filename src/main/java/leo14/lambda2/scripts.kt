package leo14.lambda2

import leo14.Script
import leo14.lineTo
import leo14.plus
import leo14.script

val Term.script: Script
	get() =
		when (this) {
			pair -> script("#pair")
			first -> script("#first")
			second -> script("#second")
			is ValueTerm -> script("value" lineTo script(value.toString()))
			is AbstractionTerm -> script("lambda" lineTo body.script)
			is ApplicationTerm -> lhs.script.plus("apply" lineTo rhs.script)
			is IndexTerm -> script("at$index")
		}
