package leo25.parser

import leo14.Script
import leo25.preprocess

val String.scriptOrNull: Script?
	get() =
		scriptParser.parsed(preprocess)

val String.scriptOrThrow: Script
	get() =
		scriptParser.parseOrThrow(preprocess)
