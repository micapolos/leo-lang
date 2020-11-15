package leo21.token.processor

import leo14.Script
import leo21.compiled.Compiled

val Script.compiled: Compiled
	get() =
		(emptyBodyTokenProcessor.plus(this) as BodyCompilerTokenProcessor).bodyCompiler.body.compiled
