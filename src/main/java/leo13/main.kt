package leo13

import leo.base.fold
import leo13.compiler.compile
import leo13.script.leo
import leo13.script.lineTo
import leo13.script.scriptWriter
import leo13.token.reader.tokenizerWriter
import leo13.untyped.interpret
import leo13.value.evaluate
import leo13.value.scriptOrNull

val typed = false
val sentences = true

fun main() {
	try {
		var tokenizer = tokenizerWriter(
			scriptWriter {
				if (!sentences) {
					val result =
						if (typed) compile?.expr?.evaluate?.scriptOrNull!!
						else interpret
					println("ok" lineTo result)
				} else {
					val evaluator = evaluator(context()).plus(sentenceScript(this))
					val script = evaluator.script.valueOrNull?.sentence ?: sentenceScript()
					val out = if (evaluator.hasError) System.err else System.out
					out.println(script)
				}
			})
		while (true) {
			val lineOrNull = readLine()
			if (lineOrNull == null) {
				tokenizer.finishWriting
				return
			} else {
				tokenizer = tokenizer.fold(lineOrNull) { write(leo(it)) }.write(leo('\n'))
			}
		}
	} catch (scriptException: ScriptException) {
		println("error" lineTo scriptException.script)
	}
}
