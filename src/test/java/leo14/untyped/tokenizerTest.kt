package leo14.untyped

import leo.base.assertEqualTo
import leo14.literal
import kotlin.test.Test

class TokenizerTest {
	@Test
	fun all() {
		context()
			.resolver()
			.tokenizer()
			.append(literal(10))
			.begin("plus")
			.append(literal(20))
			.end()!!
			.assertEqualTo(
				context()
					.resolver()
					.set(program(value(literal(30))))
					.tokenizer())
	}
}