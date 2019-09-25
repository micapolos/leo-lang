package leo13

import leo13.compiler.ExpressionTyped
import leo13.compiler.compiler
import leo13.expression.valueContext
import leo13.locator.locator
import leo13.script.lineTo
import leo13.token.Token
import leo13.tokenizer.tokenizer
import leo13.value.scriptOrNull
import leo13.value.value
import kotlin.system.exitProcess

fun main() {
	converterCapture<ExpressionTyped, Token> {
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
			print("\u001b[31m")
			println("error" lineTo this)
			print("\u001b[0m")
			exitProcess(-1)
		}
	}.let { compiled ->
		val script = valueContext().give(value()).evaluate(compiled.expression).scriptOrNull!!
		println("ok" lineTo script)
	}
}
