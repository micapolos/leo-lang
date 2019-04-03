package leo32.term

import leo.base.assertEqualTo
import leo.base.empty
import leo.base.string
import leo32.base.list
import kotlin.test.Test

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
			"body".termField)

		term.fieldList.assertEqualTo(
			list(
				"param" fieldTo term("0"),
				"param" fieldTo term("1"),
				"result" fieldTo term("i32"),
				"body".termField))

		term.at("param").assertEqualTo(list(term("0"), term("1")))
		term.at("result").assertEqualTo(list(term("i32")))
		term.at("body").assertEqualTo(list(empty.term))
	}

	@Test
	fun resolve() {
		term("one")
			.resolve { plus("resolved".termField) }
			.assertEqualTo(
				term(
					"one" fieldTo term("resolved"),
					"resolved".termField))
	}
}