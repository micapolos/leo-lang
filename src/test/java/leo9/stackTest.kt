package leo9

import leo.base.assertEqualTo
import leo.base.indexed
import kotlin.test.Test

class StackTest {
	@Test
	fun indexed() {
		stack("two", "one", "zero")
			.indexed
			.assertEqualTo(stack(2 indexed "two", 1 indexed "one", 0 indexed "zero"))
	}

	@Test
	fun zip() {
		zip(stack(2, 1, 0), stack("two", "one", "zero"))
			.assertEqualTo(stack(2 to "two", 1 to "one", 0 to "zero"))
		zip(stack(2, 1, 0), stack("one", "zero"))
			.assertEqualTo(stack(2 to null, 1 to "one", 0 to "zero"))
		zip(stack(1, 0), stack("two", "one", "zero"))
			.assertEqualTo(stack(null to "two", 1 to "one", 0 to "zero"))
	}
}