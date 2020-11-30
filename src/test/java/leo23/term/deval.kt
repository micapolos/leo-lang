package leo23.term

import leo.base.assertEqualTo
import leo.base.indexed
import leo14.number
import leo23.term.deval.deval
import leo23.term.type.booleanType
import leo23.term.type.choiceType
import leo23.term.type.nilType
import leo23.term.type.numberType
import leo23.term.type.textType
import leo23.term.type.type
import leo23.typed.of
import kotlin.test.Test

class DevalTest {
	@Test
	fun nil() {
		Unit.of(nilType).deval.assertEqualTo(nilExpr)
	}

	@Test
	fun boolean() {
		false.of(booleanType)
			.deval
			.assertEqualTo(expr(false))
	}

	@Test
	fun text() {
		"foo".of(textType)
			.deval
			.assertEqualTo(expr("foo"))
	}

	@Test
	fun number() {
		10.number
			.of(numberType)
			.deval
			.assertEqualTo(expr(10))
	}

	@Test
	fun tuple() {
		listOf(10.number, "foo")
			.of(type(numberType, textType))
			.deval
			.assertEqualTo(tuple(expr(10), expr("foo")))
	}

	@Test
	fun choice() {
		1.indexed("foo")
			.of(choiceType(numberType, textType))
			.deval
			.assertEqualTo(Expr(IndexedTerm(1, expr("foo")), choiceType(numberType, textType)))
	}
}