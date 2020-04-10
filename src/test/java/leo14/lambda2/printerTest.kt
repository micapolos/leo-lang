package leo14.lambda2

import leo.base.assertEqualTo
import kotlin.test.Test

class PrinterTest {
	@Test
	fun test() {
		term(123)
			.print
			.assertEqualTo("123")

		fn(at(0))(term(123))
			.print
			.assertEqualTo("fn { v0 -> v0 }(123)")

		fn(fn(at(0)))(term(123))(term(124))
			.print
			.assertEqualTo("fn { v0 -> fn { v1 -> v1 } }(123)(124)")

		fn(fn(at(0)))(term(123))(term(124))
			.print
			.assertEqualTo("fn { v0 -> fn { v1 -> v1 } }(123)(124)")

		fn("x", fn("y", term("x + y")))(term(123))(term(124))
			.print
			.assertEqualTo("fn { x -> fn { y -> x + y } }(123)(124)")
	}
}