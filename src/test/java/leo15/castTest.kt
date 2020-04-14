package leo15

import leo.base.assertEqualTo
import leo.base.assertNull
import leo.base.indexed
import leo14.bigDecimal
import leo14.lambda2.at
import leo14.lambda2.nil
import leo14.lambda2.valueTerm
import leo14.literal
import kotlin.test.Test

class CastTest {
	@Test
	fun literal() {
		literal(10).staticTyped.castTerm(literal(10).type).assertEqualTo(nil)
		literal(10).staticTyped.castTerm(literal(11).type).assertNull
		literal(10).staticTyped.castTerm(numberType).assertEqualTo(10.bigDecimal.valueTerm)
		literal(10).staticTyped.castTerm(textType).assertNull

		literal("foo").staticTyped.castTerm(literal("foo").type).assertEqualTo(nil)
		literal("foo").staticTyped.castTerm(literal("bar").type).assertNull
		literal("foo").staticTyped.castTerm(textType).assertEqualTo("foo".valueTerm)
		literal("foo").staticTyped.castTerm(numberType).assertNull
	}

	@Test
	fun primitives() {
		10.typed.castTerm(literal(10).type).assertNull
		10.typed.castTerm(numberType).assertEqualTo(10.bigDecimal.valueTerm)
		10.typed.castTerm(textType).assertNull

		"foo".typed.castTerm(literal("foo").type).assertNull
		"foo".typed.castTerm(textType).assertEqualTo("foo".valueTerm)
		"foo".typed.castTerm(numberType).assertNull
	}

	@Test
	fun alternatives() {
		numberType.or(textType).typed(at(0)).castTerm(numberType.or(textType)).assertEqualTo(at(0))
		"foo".typed.castTerm(numberType.or(textType)).assertEqualTo((0 indexed "foo".valueTerm).valueTerm)
		10.typed.castTerm(numberType.or(textType)).assertEqualTo((1 indexed 10.bigDecimal.valueTerm).valueTerm)
		emptyTyped.castTerm(numberType.or(textType)).assertNull
	}

	// TODO: Tests for other types.
}