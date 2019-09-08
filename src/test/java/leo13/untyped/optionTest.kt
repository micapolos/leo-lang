package leo13.untyped

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import leo13.script.unsafeValue
import org.junit.Test

class OptionTest {
	@Test
	fun reader() {
		optionReader
			.unsafeValue("option" lineTo script("zero" lineTo script("one")))
			.assertEqualTo("zero" optionTo pattern("one"))
	}
}
