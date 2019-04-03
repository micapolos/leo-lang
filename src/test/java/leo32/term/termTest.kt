package leo32.term

import leo.base.assertEqualTo
import leo.base.string
import leo32.base.i32
import kotlin.test.Test
import leo32.base.listOf

class TermTest {
	@Test
	fun string() {
		term(
			"circle" fieldTo term(
				"radius" fieldTo term("10"),
				"center" fieldTo term(
					"x" fieldTo term("12"),
					"y" fieldTo term("13"))))
			.string
			.assertEqualTo("circle(radius(10).center(x(12).y(13)))")
	}

	@Test
	fun at() {
		val term = term(
			"param" fieldTo term("0"),
			"param" fieldTo term("1"),
			"result" fieldTo term("i32"),
			"body" fieldTo emptyTerm)

		term.fieldCount.assertEqualTo(4.i32)
		term.at(0.i32).assertEqualTo("param" fieldTo term("0"))
		term.at(1.i32).assertEqualTo("param" fieldTo term("1"))
		term.at(2.i32).assertEqualTo("result" fieldTo term("i32"))
		term.at(3.i32).assertEqualTo("body" fieldTo emptyTerm)

		term.at("param").assertEqualTo(listOf(term("0"), term("1")))
		term.at("result").assertEqualTo(listOf(term("i32")))
		term.at("body").assertEqualTo(listOf(emptyTerm))
	}
}