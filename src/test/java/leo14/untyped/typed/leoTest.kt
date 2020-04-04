package leo14.untyped.typed

import leo.base.assertEqualTo
import leo14.invoke
import leo14.leo
import kotlin.test.Test

class LeoTest {
	@Test
	fun text() {
		leo("Hello, world!")
			.eval
			.assertEqualTo(leo("Hello, world!"))
	}

	@Test
	fun textPlus() {
		leo("Hello, ", "plus"("world!"))
			.eval
			.assertEqualTo(leo("Hello, world!"))
	}
}