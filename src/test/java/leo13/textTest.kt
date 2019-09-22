package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class TextTest {
	@Test
	fun stringText() {
		text("a()")
			.assertEqualTo(
				text()
					.plus(character('a'))
					.plus(character('('))
					.plus(character(')')))
	}
}
