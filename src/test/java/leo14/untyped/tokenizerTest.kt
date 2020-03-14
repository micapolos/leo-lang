package leo14.untyped

import leo.base.assertEqualTo
import leo14.line
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class TokenizerTest {
	@Test
	fun all() {
		context()
			.interpreter()
			.tokenizer()
			.plus(literal(10))
			.begin("plus")
			.plus(literal(20))
			.end()!!
			.assertEqualTo(
				context()
					.interpreter()
					.set(script(line(literal(10)), "plus" lineTo script(literal(20))))
					.tokenizer())
	}
}