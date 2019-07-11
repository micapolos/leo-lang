package lambda.v2

import leo.base.assertEqualTo
import leo.base.nat
import leo.base.string
import kotlin.test.Test

class TermTest {
	@Test
	fun string() {
		fn(1) { arg(1) }.string.assertEqualTo("x0 -> x0")
		fn(2) { arg(1) }.string.assertEqualTo("x0 -> x1 -> x0")
		fn(2) { arg(2) }.string.assertEqualTo("x0 -> x1 -> x1")
		term(argument(0.nat)).string.assertEqualTo("?1")
		term(argument(0.nat))(term(argument(1.nat))).string.assertEqualTo("?1(?2)")
	}

	@kotlin.test.Test
	fun functionTermOrNull() {
		fn(3){ id }.functionTermOrNull(3).assertEqualTo(id)
	}
}