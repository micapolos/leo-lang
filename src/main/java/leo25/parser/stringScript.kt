package leo25.parser

import leo14.Script
import leo25.notNullOrThrow
import leo25.notationName
import leo25.preprocess
import leo25.value

val String.scriptOrNull: Script?
	get() =
		scriptParser.parsed(preprocess)

val String.scriptOrThrow: Script
	get() =
		scriptOrNull.notNullOrThrow { value(notationName) }
