package leo14.typed.compiler.natives

import leo14.lambda.Term
import leo14.lambda.scriptLine
import leo14.native.Native
import leo14.scriptLine

val Term<Native>.termDecompile
	get() =
		scriptLine(Native::scriptLine)