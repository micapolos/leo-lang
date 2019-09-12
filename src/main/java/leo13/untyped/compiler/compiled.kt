package leo13.untyped.compiler

import leo13.script.Script
import leo13.script.plus
import leo13.script.script
import leo13.untyped.Pattern
import leo13.untyped.pattern
import leo13.untyped.plus

data class Compiled(val script: Script, val pattern: Pattern)

fun compiled(script: Script = script(), pattern: Pattern = pattern()) =
	Compiled(script, pattern)

fun Compiled.plus(line: CompiledLine): Compiled =
	compiled(
		script.plus(line.scriptLine),
		pattern.plus(line.patternLine))