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

		term.invoke(selector())
			.assertEqualTo(term)
		term.invoke(selector("circle".getter, "radius".getter))
			.assertEqualTo(term("10"))
		term.invoke(selector("circle".getter, "center".getter))
			.assertEqualTo(term("x" fieldTo term("12"), "y" fieldTo term("13")))
		term.invoke(selector("circle".getter, "center".getter, "x".getter))
			.assertEqualTo(term("12"))
		term.invoke(selector("circle".getter, "center".getter, "y".getter))
			.assertEqualTo(term("13"))
	}
}