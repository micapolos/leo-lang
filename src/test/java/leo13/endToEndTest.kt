package leo13

import leo.base.assertEqualTo
import leo13.compiler.Compiled
import leo13.compiler.compiled
import leo13.compiler.compiler
import leo13.expression.expression
import leo13.interpreter.Interpreted
import leo13.interpreter.interpreted
import leo13.interpreter.interpreter
import leo13.pattern.pattern
import leo13.token.Token
import leo13.tokenizer.tokenizer
import leo13.value.value
import kotlin.test.Test

class EndToEndTest {
	@Test
	fun interpreting() {
		converterCapture<Interpreted, Token> {
			interpreter().tokenizer().charProcess("jajko\n").process(endOfTransmissionChar)
		}.assertEqualTo(interpreted(value("jajko"), pattern("jajko")))
	}

	@Test
	fun compiling() {
		converterCapture<Compiled, Token> {
			compiler().tokenizer().charProcess("jajko\n").process(endOfTransmissionChar)
		}.assertEqualTo(
			compiled(
				expression("jajko"),
				pattern("jajko")))
	}
}
