package leo14.untyped

import leo.base.assertEqualTo
import leo14.line
import leo14.literal
import leo14.script
import kotlin.test.Test

class TokenizerTest {
	@Test
	fun all() {
		context()
			.liner()
			.tokenizer()
			.append(literal(10))
			.begin("plus")
			.append(literal(20))
			.end()!!
			.assertEqualTo(
				context()
					.liner()
					.set(script(line(literal(30))))
					.tokenizer())
	}
}