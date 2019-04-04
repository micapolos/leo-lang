package leo32.runtime

import leo.base.assertEqualTo
import leo.base.empty
import leo.base.string
import leo32.base.list
import kotlin.test.Test

class TermTest {
	@Test
	fun string() {
		term(
			"circle" to term(
				"radius" to term("10"),
				"center" to term(
					"x" to term("12"),
					"y" to term("13"))))
			.string
			.assertEqualTo("circle(radius(10).center(x(12).y(13)))")
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
//
//	@Test
//	fun resolve() {
//		val resolveFn: Term.() -> Term = { term("resolved" fieldTo this) }
//
//		empty.term.resolve(resolveFn).assertEqualTo(empty.term)
//
//		term("one").resolve(resolveFn).assertEqualTo(term("resolved" fieldTo term("one")))
//
//		term(
//			"one".termField,
//			"plus" fieldTo term("two"))
//			.resolve(resolveFn)
//			.assertEqualTo(
//				term(
//					"resolved" fieldTo term("one"),
//					"plus" fieldTo term("resolved" fieldTo term("two"))))
//	}
}