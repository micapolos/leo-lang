package leo21.token.processor

import leo14.Script
import leo14.rootScript

val TokenProcessor.script: Script
	get() =
		fragment.rootScript