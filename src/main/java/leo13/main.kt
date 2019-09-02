package leo13

import leo13.compiler.compile
import leo13.script.lineTo
import leo13.script.parse
import leo13.token.reader.finish
import leo13.token.reader.push
import leo13.token.reader.tokenizer
import leo13.value.evaluate
import leo13.value.scriptOrNull

fun main(args: Array<String>) {
	try {
		var tokenizer = tokenizer()
		while (true) {
			val lineOrNull = readLine()
			if (lineOrNull == null) {
				println("ok" lineTo tokenizer.finish.parse?.compile?.expr?.evaluate?.scriptOrNull!!)
				return
			} else tokenizer = tokenizer.push(lineOrNull).push('\n')
		}
	} catch (scriptException: ScriptException) {
		println("error" lineTo scriptException.script)
	}
}
