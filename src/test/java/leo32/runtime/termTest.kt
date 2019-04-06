package leo32.runtime

import leo.base.*
import leo32.base.list
import leo32.string32
import kotlin.test.Test

class TermTest {
	@Test
	fun simpleString() {
		term("color" to term("red"))
			.string
			.assertEqualTo("color red")

		term("pencil" to term("color" to term("red")))
			.string
			.assertEqualTo("pencil color red")

		term("pencil" to term("red" to term(), "color" to term()))
			.string
			.assertEqualTo("pencil(red, color)")

		term("red" to term(), "color" to term(), "pencil" to term())
			.string
			.assertEqualTo("red, color, pencil")

		term("x" to term("10"), "y" to term("12"))
			.string
			.assertEqualTo("x 10, y 12")

		term("vec" to term("x" to term("10"), "y" to term("12")))
			.string
			.assertEqualTo("vec(x 10, y 12)")

		term("center" to term("vec" to term("x" to term("10"), "y" to term("12"))))
			.string
			.assertEqualTo("center vec(x 10, y 12)")

		term("vec" to term("x" to term("percent" to term("10")), "y" to term("percent" to term("12"))))
			.string
			.assertEqualTo("vec(x percent 10, y percent 12)")

		term(
			"vec" to term("x" to term("10"), "y" to term("12")),
			"vec" to term("x" to term("13"), "y" to term("14")))
			.string
			.assertEqualTo("vec(x 10, y 12), vec(x 13, y 14)")

		term("red" to term(), "color" to term())
			.string
			.assertEqualTo("red, color")

		term(
			"circle" to term(
				"radius" to term("10"),
				"center" to term(
					"x" to term("12"),
					"y" to term("13"))))
			.string
			.assertEqualTo("circle(radius 10, center(x 12, y 13))")
	}

	@Test
	fun at() {
		val term = term(
			"param" to term("0"),
			"param" to term("1"),
			"result" to term("i32"),
			"body" to term())

		term.fieldList.assertEqualTo(
			list(
				"param" to term("0"),
				"param" to term("1"),
				"result" to term("i32"),
				"body" to term()))

		term.at("param").assertEqualTo(list(term("0"), term("1")))
		term.at("result").assertEqualTo(list(term("i32")))
		term.at("body").assertEqualTo(list(empty.term))
	}

	@Test
	fun resolve() {
		lateinit var resolveFn: Term.() -> Term
		resolveFn = {
			if (isEmpty) this
			else term("resolved" to map(resolveFn))
		}

		empty.term.map(resolveFn).assertEqualTo(empty.term)

		term("one")
			.map(resolveFn)
			.assertEqualTo(term("one"))

		term(
			"one" to term("jeden"),
			"two" to term("dwa"))
			.map(resolveFn)
			.assertEqualTo(
				term(
					"resolved" to term(
						"one" to term(
							"resolved" to term("jeden"))),
					"two" to term(
						"resolved" to term("dwa"))))
	}

	@Test
	fun evalGet() {
		term()
			.evalGet
			.assertEqualTo(term())

		term("one")
			.evalGet
			.assertEqualTo(term("one"))

		term(
			"one" to term(),
			"one" to term())
			.evalGet
			.assertEqualTo(term())

		term(
			"one" to term("jeden"),
			"one" to term())
			.evalGet
			.assertEqualTo(term("jeden"))

		term(
			"one" to term("jeden"),
			"two" to term())
			.evalGet
			.assertEqualTo(
				term(
					"one" to term("jeden"),
					"two" to term()))

		term(
			"circle" to term("radius" to term("10")),
			"circle" to term())
			.evalGet
			.assertEqualTo(term("radius" to term("10")))

		term(
			"circle" to term("radius" to term("10")),
			"circle" to term("radius"))
			.evalGet
			.assertEqualTo(term("10"))

		term(
			"circle" to term("radius" to term("10")),
			"circle" to term("center"))
			.evalGet
			.assertEqualTo(
				term(
					"radius" to term("10"),
					"center" to term()))
	}

	@Test
	fun evalWrap() {
		term()
			.evalWrap
			.assertEqualTo(term())

		term("one")
			.evalWrap
			.assertEqualTo(term("one"))

		term(
			"red" to term(),
			"color" to term())
			.evalWrap
			.assertEqualTo(term("color" to term("red")))

		term(
			"two" to term(),
			"plus" to term("two"))
			.evalWrap
			.assertEqualTo(
				term(
					"two" to term(),
					"plus" to term("two")))
	}

	@Test
	fun seq32() {
		term(
			"circle" to term(
				"radius" to term("10"),
				"center" to term(
					"x" to term("1.2"),
					"y" to term("1.3"))))
			.seq32
			.string32
			.assertEqualTo("circle radius 10   center x 1.2   y 1.3     ")
	}

	@Test
	fun seq32_escaping() {
		term("ala ma kota").seq32.string32.assertEqualTo("ala\\_ma\\_kota  ")
		term("foo\\bar").seq32.string32.assertEqualTo("foo\\\\bar  ")
	}

	@Test
	fun eval() {
		term(
			"x" to term("0"),
			"x" to term())
			.invoke(empty.scope)
			.assertEqualTo(term("0"))
	}
}