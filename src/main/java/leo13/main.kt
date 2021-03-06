package leo13

import leo.java.io.charSeq
import leo13.decompiler.decompiler
import leo13.interpreter.*
import leo13.locator.locator
import leo13.script.script
import leo13.tokenizer.tokenizer
import kotlin.system.exitProcess

fun main() {
	processorUpdate(script()) {
		traced {
			System
				.`in`
				.use { inputStream ->
					decompiler()
						.map<Interpreted, ValueTyped> { typed }
						.interpreter(interpreted(coreInterpreterContext))
						.tokenizer()
						.locator()
						.process(inputStream.reader().charSeq)
						.process(endOfTransmissionChar)
				}
		}.onError {
			print("\u001b[31m")
			println(this)
			print("\u001b[0m")
			exitProcess(-1)
		}
	}.let { script ->
		println(script)
	}
}
