package leo13.untyped.compiler

import leo13.script.Script
import leo13.script.script
import leo13.untyped.Pattern
import leo13.untyped.pattern

data class Compiled(val script: Script, val pattern: Pattern)

fun value(script: Script = script(), pattern: Pattern = pattern()) =
	Compiled(script, pattern)