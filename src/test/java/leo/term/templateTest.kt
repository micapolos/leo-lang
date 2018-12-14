package leo.term

import leo.base.assertEqualTo
import leo.base.string
import leo.firstWord
import leo.nameWord
import leo.personWord
import kotlin.test.Test

class TemplateTest {
	@Test
	fun string() {
		template(
			personWord apply template(
				firstWord apply template(nameWord.template)))
			.string
			.assertEqualTo("person first name")
	}
}