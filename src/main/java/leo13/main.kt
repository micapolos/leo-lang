package leo13

import leo13.compiler.compiler
import leo13.interpreter.interpreter
import leo13.interpreter.valueTyped
import leo13.locator.locator
import leo13.script.lineTo
import leo13.tokenizer.tokenizer
import leo13.value.scriptOrNull
import kotlin.system.exitProcess

fun main() {
	processorUpdate(valueTyped()) {
		traced {
			var charProcessor: Processor<Char> = interpreter().compiler().tokenizer().locator()
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
	}.let { typed ->
		val script = typed.value.scriptOrNull!!
		println("ok" lineTo script)
	}
}
