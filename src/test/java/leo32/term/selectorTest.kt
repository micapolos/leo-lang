package leo32.term

import leo.base.assertEqualTo
import kotlin.test.Test

class SelectorTest {
	@Test
	fun invoke() {
		val term = term(
			"circle" fieldTo term(
				"radius" fieldTo term("10"),
				"center" fieldTo term(
					"x" fieldTo term("12"),
					"y" fieldTo term("13"))))

		selector()
			.invoke(term)
			.assertEqualTo(term)
		selector("circle".getter, "radius".getter)
			.invoke(term)
			.assertEqualTo(term("10"))
		selector("circle".getter, "center".getter)
			.invoke(term)
			.assertEqualTo(term("x" fieldTo term("12"), "y" fieldTo term("13")))
		selector("circle".getter, "center".getter, "x".getter)
			.invoke(term)
			.assertEqualTo(term("12"))
		selector("circle".getter, "center".getter, "y".getter)
			.invoke(term)
			.assertEqualTo(term("13"))
	}
}