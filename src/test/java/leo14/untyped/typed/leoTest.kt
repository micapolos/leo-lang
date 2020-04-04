package leo14.untyped.typed

import leo.base.assertEqualTo
import leo14.invoke
import leo14.leo
import kotlin.test.Test

class LeoTest {
	@Test
	fun empty() {
		leo().eval.assertEqualTo(leo())
	}

	@Test
	fun text() {
		leo("Hello, world!").eval.assertEqualTo(leo("Hello, world!"))
	}

	@Test
	fun number() {
		leo(10).eval.assertEqualTo(leo(10))
	}

	@Test
	fun textPlusText() {
		leo("Hello, ", "plus"("world!"))
			.eval
			.assertEqualTo(leo("Hello, world!"))
	}

	@Test
	fun numberPlusNumber() {
		leo(2, "plus"(3))
			.eval
			.assertEqualTo(leo(5))
	}

	@Test
	fun numberMinusNumber() {
		leo(5, "minus"(3))
			.eval
			.assertEqualTo(leo(2))
	}

	@Test
	fun numberTimesNumber() {
		leo(2, "times"(3))
			.eval
			.assertEqualTo(leo(6))
	}
}