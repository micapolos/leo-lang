package leo21.token.compiler

import leo14.Script
import leo21.type.script

val TokenCompiler.script: Script
	get() =
		lineCompiler.compiled.type.script