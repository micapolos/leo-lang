package leo15.lambda

import leo.base.assertEqualTo
import kotlin.test.Test

class PrinterTest {
	@Test
	fun test() {
		value(123)
			.print
			.assertEqualTo("123")

		fn(at(0))(value(123))
			.print
			.assertEqualTo("{ v0 -> v0 }(123)")

		fn(fn(at(0)))(value(123))(value(124))
			.print
			.assertEqualTo("{ v0 -> { v1 -> v1 } }(123)(124)")

		fn(fn(at(0)))(value(123))(value(124))
			.print
			.assertEqualTo("{ v0 -> { v1 -> v1 } }(123)(124)")

		fn("x", fn("y", value("x + y")))(value(123))(value(124))
			.print
			.assertEqualTo("{ x -> { y -> x + y } }(123)(124)")
	}
}