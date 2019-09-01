package leo13.base

import leo.base.assertEqualTo
import leo13.token.Token
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import kotlin.test.Test

class WriterTest {
	@Test
	fun write() {
		writer<Token>()
			.write(token(opening("foo")))
			.write(token(closing))
			.assertEqualTo(
				writer(
					token(opening("foo")),
					token(closing)))
	}
}
