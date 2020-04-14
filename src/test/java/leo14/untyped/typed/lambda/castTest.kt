package leo14.untyped.typed.lambda

import leo.base.assertEqualTo
import leo.base.assertNull
import leo14.literal
import leo14.untyped.typed.numberType
import leo14.untyped.typed.textType
import leo14.untyped.typed.type
import kotlin.test.Test

class CastTest {
	@Test
	fun literal() {
		literal(10).staticTyped.cast(literal(10).type).assertEqualTo(literal(10).staticTyped)
		literal(10).staticTyped.cast(literal(11).type).assertNull
		literal(10).staticTyped.cast(numberType).assertEqualTo(10.typed)
		literal(10).staticTyped.cast(textType).assertNull

		literal("foo").staticTyped.cast(literal("foo").type).assertEqualTo(literal("foo").staticTyped)
		literal("foo").staticTyped.cast(literal("bar").type).assertNull
		literal("foo").staticTyped.cast(textType).assertEqualTo("foo".typed)
		literal("foo").staticTyped.cast(numberType).assertNull
	}

	@Test
	fun primitives() {
		10.typed.cast(literal(10).type).assertNull
		10.typed.cast(numberType).assertEqualTo(10.typed)
		10.typed.cast(textType).assertNull

		"foo".typed.cast(literal("foo").type).assertNull
		"foo".typed.cast(textType).assertEqualTo("foo".typed)
		"foo".typed.cast(numberType).assertNull
	}
}