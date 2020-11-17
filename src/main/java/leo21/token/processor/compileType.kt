package leo21.token.processor

import leo14.Script
import leo21.type.Type

val Script.compileType: Type
	get() =
		emptyTypeProcessor.plus(this).type