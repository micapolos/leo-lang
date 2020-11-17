package leo21.token.processor

import leo14.Script
import leo14.rootScript

val Processor.script: Script
	get() =
		fragment.rootScript