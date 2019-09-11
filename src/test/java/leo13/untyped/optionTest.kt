package leo13.untyped

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import org.junit.Test

class EitherTest {
	@Test
	fun reader() {
		("either" lineTo script("zero" lineTo script("one")))
			.unsafeEither
			.assertEqualTo("zero" eitherTo pattern("one"))
	}
}
