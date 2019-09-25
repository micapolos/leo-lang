package leo13

import leo.base.assertEqualTo
import leo13.compiler.compiler
import leo13.compiler.expressionTyped
import leo13.compiler.typed
import leo13.expression.expression
import leo13.interpreter.interpreter
import leo13.interpreter.typed
import leo13.interpreter.valueTyped
import leo13.tokenizer.tokenizer
import leo13.type.type
import leo13.value.value
import kotlin.test.Test

class EndToEndTest {
	@Test
	fun interpreting() {
		processorUpdate(valueTyped()) {
			interpreter().compiler().tokenizer().charProcess("jajko\n").process(endOfTransmissionChar)
		}.assertEqualTo(typed(value("jajko"), type("jajko")))
	}

	@Test
	fun compiling() {
		processorUpdate(expressionTyped()) {
			compiler().tokenizer().charProcess("jajko\n").process(endOfTransmissionChar)
		}.assertEqualTo(typed(expression("jajko"), type("jajko")))
	}
}
