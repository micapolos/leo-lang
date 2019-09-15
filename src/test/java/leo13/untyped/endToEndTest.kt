package leo13.untyped

import leo.base.assertEqualTo
import leo13.charProcess
import leo13.converterCapture
import leo13.endOfTransmissionChar
import leo13.token.Token
import leo13.token.reader.tokenizer
import leo13.untyped.compiler.Compiled
import leo13.untyped.compiler.compiler
import leo13.untyped.interpreter.interpreter
import leo13.untyped.normalizer.normalizer
import kotlin.test.Test

class EndToEndTest {
	@Test
	fun interpreting() {
		interpreter().normalizer().tokenizer().charProcess("jajko\n")
	}

	@Test
	fun compiling() {
		converterCapture<Compiled, Token> {
			compiler().normalizer().tokenizer().charProcess("jajko\n").process(endOfTransmissionChar)
		}.assertEqualTo(null)
	}
}
