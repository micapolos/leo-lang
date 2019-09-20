package leo13

import leo.base.assertEqualTo
import leo13.compiler.Compiled
import leo13.compiler.compiled
import leo13.compiler.compiler
import leo13.expression.expression
import leo13.pattern.pattern
import leo13.token.Token
import leo13.tokenizer.tokenizer
import kotlin.test.Test

class EndToEndTest {
//	@Test
//	fun interpreting() {
//		converterCapture<Interpreted, Token> {
//			interpreter().normalizer().tokenizer().charProcess("jajko\n").process(endOfTransmissionChar)
//		}.assertEqualTo(null)
//	}

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
