package leo32.term

import leo.base.assertEqualTo
import kotlin.test.Test

class TemplateTest {
	@Test
	fun invoke() {
		val template =
			template(
				selector("circle".getter, "radius".getter),
				"times" fieldTo template(selector("circle".getter, "radius".getter)),
				"times" fieldTo template("pi"))

		val term =
			term(
				"circle" fieldTo term(
					"radius" fieldTo term("10")))

		template.invoke(term).assertEqualTo(
			term(
				"10".termField,
				"times" fieldTo term("10"),
				"times" fieldTo term("pi")))
	}
}