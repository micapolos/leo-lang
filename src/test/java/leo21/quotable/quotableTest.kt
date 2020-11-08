package leo21.quotable

import leo.base.assertEqualTo
import kotlin.test.Test

class QuotableTest {
	@Test
	fun all() {
		10.quotable.quote.quote.unquote.unquote.value.assertEqualTo(10)
	}
}