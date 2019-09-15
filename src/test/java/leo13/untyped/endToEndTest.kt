package leo13.untyped

import leo13.charProcess
import leo13.token.reader.tokenizer
import leo13.untyped.compiler.compiler
import leo13.untyped.normalizer.normalizer
import kotlin.test.Test

class EndToEndTest {
	@Test
	fun process() {
		compiler().normalizer().tokenizer().charProcess("jajko\n")
	}
}