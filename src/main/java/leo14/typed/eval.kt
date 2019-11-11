package leo14.typed

import leo14.Script
import leo14.lambda.eval

val Typed<Any>.anyEval: Typed<Any>
	get() =
		term.eval of type

val Script.anyEval: Script
	get() =
		anyCompile.anyEval.anyDecompile
