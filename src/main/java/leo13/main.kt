package leo13

import leo13.compiler.compile
import leo13.script.lineTo
import leo13.script.parse
import leo13.token.reader.finish
import leo13.token.reader.push
import leo13.token.reader.reader
import leo13.value.evaluate
import leo13.value.scriptOrNull

fun main(args: Array<String>) {
	try {
		var reader = reader()
		while (true) {
			val lineOrNull = readLine()
			if (lineOrNull == null) {
				println("ok" lineTo reader.finish.parse?.compile?.expr?.evaluate?.scriptOrNull!!)
				return
			} else reader = reader.push(lineOrNull).push('\n')
		}
	} catch (scriptException: ScriptException) {
		println("error" lineTo scriptException.script)
	}
}
