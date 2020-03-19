package leo14.untyped

import leo.base.assertEqualTo
import leo14.begin
import leo14.end
import kotlin.test.Test

class TokenizerTest {
	@Test
	fun simple() {
		emptyTokenizer
			.write(begin("foo"))!!
			.write(end)
			.assertEqualTo(emptyTokenizer.write("foo" fieldTo program()))
	}
}