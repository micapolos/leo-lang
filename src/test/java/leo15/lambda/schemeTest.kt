package leo15.lambda

import leo.base.assertEqualTo
import kotlin.test.Test

class SchemeTest {
	@Test
	fun test() {
		value(123)
			.scheme
			.assertEqualTo("123")

		fn(at(0))
			.scheme
			.assertEqualTo("(lambda (v0) v0)")

		fn(at(0))(value(123))
			.scheme
			.assertEqualTo("((lambda (v0) v0) 123)")

		fn(fn(at(0)))(value(123))(value(124))
			.scheme
			.assertEqualTo("(((lambda (v0) (lambda (v1) v1)) 123) 124)")

		fn(fn(at(1)))(value(123))(value(124))
			.scheme
			.assertEqualTo("(((lambda (v0) (lambda (v1) v0)) 123) 124)")
	}
}