package leo21.token.processor

import leo14.Script
import leo21.compiled.Compiled

val Script.compiled: Compiled
	get() =
		(emptyBodyProcessor.plus(this) as BodyCompilerProcessor).bodyCompiler.body.compiled
