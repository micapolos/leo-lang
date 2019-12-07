package leo14.typed.compiler.natives

import leo14.lambda.Term
import leo14.lambda.scriptLine
import leo14.native.Native

val Term<Native>.termDecompile
	get() =
		scriptLine // TODO