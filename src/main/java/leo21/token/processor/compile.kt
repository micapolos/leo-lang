package leo21.token.processor

import leo14.Script
import leo21.compiled.script

val Script.compile: Script
	get() =
		emptyCompilerTokenProcessor.plus(this).compiled.script
