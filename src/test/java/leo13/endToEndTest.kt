package leo13

import leo.base.assertEqualTo
import leo13.decompiler.decompiler
import leo13.interpreter.Interpreted
import leo13.interpreter.ValueTyped
import leo13.interpreter.interpreter
import leo13.script.script
import leo13.tokenizer.tokenizer
import kotlin.test.Test

class EndToEndTest {
	@Test
	fun endToEnd() {
		processorUpdate(script()) {
			decompiler()
				.map<Interpreted, ValueTyped> { typed }
				.interpreter()
				.tokenizer()
				.charProcess("jajko\n")
				.process(endOfTransmissionChar)
		}.assertEqualTo(script("jajko"))
	}
}
