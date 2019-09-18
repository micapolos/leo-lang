package leo13

import leo13.locator.locator
import leo13.script.lineTo
import leo13.token.Token
import leo13.token.reader.tokenizer
import leo13.untyped.compiler.Compiled
import leo13.untyped.compiler.compiler
import leo13.untyped.expression.evaluate
import leo13.untyped.expression.given
import leo13.untyped.value.scriptOrNull
import leo13.untyped.value.value
import kotlin.system.exitProcess

fun main() {
	converterCapture<Compiled, Token> {
		traced {
			var charProcessor: Processor<Char> = compiler().tokenizer().locator()
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
		}.onError {
			println("error" lineTo this)
			exitProcess(-1)
		}
	}.let { compiled ->
		val script = given(value()).evaluate(compiled.expression).scriptOrNull!!
		println(script)
	}
}
