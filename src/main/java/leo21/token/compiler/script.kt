package leo21.token.compiler

import leo13.array
import leo13.map
import leo14.Script
import leo14.script
import leo21.type.script
import leo21.type.scriptLine

val TokenCompiler.script: Script
	get() =
		lineCompiler.compiled.type.script

val DataCompiler.script: Script
	get() =
		script(*lineCompiledStack.map { line.scriptLine }.array)
