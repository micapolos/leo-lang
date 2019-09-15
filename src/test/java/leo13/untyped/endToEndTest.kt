package leo13.untyped

import leo13.charProcess
import leo13.token.reader.tokenizer
import leo13.untyped.interpreter.interpreter
import leo13.untyped.normalizer.normalizer
import kotlin.test.Test

class EndToEndTest {
	@Test
	fun process() {
		interpreter().normalizer().tokenizer().charProcess("jajko\n")
	}
}