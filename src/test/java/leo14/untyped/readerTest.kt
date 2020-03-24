package leo14.untyped

import leo.base.assertEqualTo
import leo14.begin
import leo14.end
import leo14.token
import kotlin.test.Test

class ReaderTest {
	@Test
	fun name() {
		emptyReader
			.write(token(begin("foo")))!!
			.write(token(end))!!
			.assertEqualTo(
				UnquotedReader(Unquoted(null, context().resolver(value("foo" lineTo value())))))
	}
}