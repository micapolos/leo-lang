package leo23.typed.term

import leo14.Script
import leo14.ScriptLine
import leo23.type.script
import leo23.type.scriptLine

val StackCompiled.printScript: Script
	get() =
		t.script

val Compiled.printScriptLine: ScriptLine
	get() =
		t.scriptLine