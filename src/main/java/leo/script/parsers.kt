package leo.script

import leo.binary.booleanParser
import leo.binary.map

val booleanScriptParser =
	booleanParser.map { it.script }