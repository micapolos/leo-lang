package leo13.pattern

import leo.base.assertEqualTo
import leo13.eitherName
import leo13.script.lineTo
import leo13.script.script
import org.junit.Test

class EitherTest {
	@Test
	fun eitherScriptLine() {
		"zero"
			.eitherTo(pattern("foo"))
			.scriptLine
			.assertEqualTo(eitherName lineTo pattern("zero" lineTo pattern("foo")).bodyScript)
	}

	@Test
	fun reader() {
		("either" lineTo script("zero" lineTo script("one")))
			.unsafeEither
			.assertEqualTo("zero" eitherTo pattern("one"))
	}
}
