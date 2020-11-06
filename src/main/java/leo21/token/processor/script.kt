package leo21.token.processor

import leo14.Script
import leo21.token.compiler.script

val TokenProcessor.script: Script
	get() =
		when (this) {
			is CompilerTokenProcessor -> compiler.script
		}