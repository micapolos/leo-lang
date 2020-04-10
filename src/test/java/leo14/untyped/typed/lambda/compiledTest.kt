package leo14.untyped.typed.lambda

import leo.base.assertEqualTo
import leo14.lambda2.*
import leo14.untyped.typed.*
import kotlin.test.Test

class CompiledTest {
	@Test
	fun plus_static_static() {
		type("foo").compiled(nil)
			.plus("plus".lineTo(type("bar")).compiled(nil))
			.assertEqualTo(
				type("foo")
					.plus("plus" lineTo type("bar"))
					.compiled(nil))
	}

	@Test
	fun plus_static_dynamic() {
		type("foo").compiled(nil)
			.plus(textTypeLine.compiled(value("world!")))
			.assertEqualTo(
				type("foo")
					.plus(textTypeLine)
					.compiled(value("world!")))
	}

	@Test
	fun plus_dynamic_static() {
		textType.compiled(value("Hello, "))
			.plus("plus".lineTo(type("bar")).compiled(nil))
			.assertEqualTo(
				textType
					.plus("plus" lineTo type("bar"))
					.compiled(value("Hello, ")))
	}

	@Test
	fun plus_dynamic_dynamic() {
		textType.compiled(value("Hello, "))
			.plus(textTypeLine.compiled(value("world!")))
			.assertEqualTo(
				textType
					.plus(textTypeLine)
					.compiled(pair(value("Hello, "))(value("world!"))))
	}

	@Test
	fun matchLink_static_static() {
		type("foo")
			.plus("plus" lineTo type("bar"))
			.compiled(nil)
			.matchLink { line ->
				textType.compiled(value("OK"))
			}
			.assertEqualTo(textType.compiled(value("OK")))
	}

	@Test
	fun matchLink_dynamic_dynamic() {
		textType
			.plus(textTypeLine)
			.compiled(value("pair"))
			.matchLink { line ->
				textType.compiled(value("+"))
			}
			.assertEqualTo(textType.compiled(fn(fn(fn(value("+")))(at(0)(first))(at(0)(second)))(value("pair"))))
	}

	@Test
	fun evaluatesOnce() {
		val sumTerm = fn { lhs -> fn { rhs -> value(lhs.value.asString + rhs.value.asString) } }.assertEvaluatesOnce
		textType.compiled(value("Hello, ").assertEvaluatesOnce)
			.plus(("plus" lineTo textType).compiled(value("world!").assertEvaluatesOnce))
			.matchInfix("plus") { rhs ->
				textType.compiled(sumTerm(term)(rhs.term))
			}!!
			.term
			.eval
			.value
			.assertEqualTo("Hello, world!")
	}
}