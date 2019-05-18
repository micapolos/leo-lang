package leo3

import leo.base.assertEqualTo
import kotlin.test.Test

class TemplateTest {
	@Test
	fun argumentTemplate() {
		template(argument)
			.apply(parameter(value(line(word("foo"), value()))))
			.assertEqualTo(value(line(word("foo"), value())))
	}

	@Test
	fun valueTemplate() {
		template(value(line(word("foo"), value())))
			.apply(parameter(value(line(word("bar"), value()))))
			.assertEqualTo(value(line(word("foo"), value())))
	}

	@Test
	fun selectorTemplate() {
		template(selector(template(argument), getter(rhs)))
			.apply(parameter(value(line("foo", value("bar")))))
			.assertEqualTo(value("bar"))
	}
}