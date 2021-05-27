package leo25

import leo.base.orIfNull
import leo14.Script
import leo25.natives.nativeDictionary
import leo25.parser.scriptOrNull

val String.interpret: String
	get() =
		scriptOrNull
			?.run { interpret }
			.orIfNull { value(quoteName).errorValue.script }
			.string

val Script.interpret: Script
	get() =
		environment().interpret(this)

fun Environment.interpret(script: Script): Script =
	try {
		script.interpretLeo.run(this).value
	} catch (e: Throwable) {
		e.value.script
	}

val Script.interpretLeo: Leo<Script>
	get() =
		nativeDictionary.valueLeo(this).map { it.script }

