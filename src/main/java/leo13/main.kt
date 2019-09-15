package leo13

import leo13.locator.locator
import leo13.token.Token
import leo13.token.reader.tokenizer
import leo13.untyped.compiler.Compiled
import leo13.untyped.compiler.compiler
import leo13.untyped.expression.evaluate
import leo13.untyped.expression.given
import leo13.untyped.normalizer.normalizer
import leo13.untyped.value.scriptOrNull
import leo13.untyped.value.value

fun main() {
	converterCapture<Compiled, Token> {
		var charProcessor: Processor<Char> = compiler().normalizer().tokenizer().locator()
		val reader = System.`in`.reader()
		while (true) {
			val charInt = reader.read()
			if (charInt == -1) {
				charProcessor = charProcessor.process(endOfTransmissionChar)
				break
			}
			charProcessor = charProcessor.process(charInt.toChar())
		}
		charProcessor
	}.let { compiled ->
		val script = given(value()).evaluate(compiled.expression).scriptOrNull!!
		println(script)
	}
}
