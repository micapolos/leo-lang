package leo3

import leo.base.assertEqualTo
import kotlin.test.Test

class TemplateTest {
	@Test
	fun argumentTemplate() {
		template(argument)
			.apply(parameter(value(line(word("one"), value()))))
			.assertEqualTo(value(line(word("one"), value())))
	}
}