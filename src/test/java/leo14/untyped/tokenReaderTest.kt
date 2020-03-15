package leo14.untyped

import leo.base.assertEqualTo
import leo14.literal
import kotlin.test.Test

class TokenReaderTest {
	@Test
	fun all() {
		context()
			.resolver()
			.tokenReader()
			.append(literal(10))
			.begin("plus")
			.append(literal(20))
			.end()!!
			.assertEqualTo(
				context()
					.resolver()
					.set(program(value(literal(30))))
					.tokenReader())
	}
}