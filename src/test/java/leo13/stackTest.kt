package leo13

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
		leo13.zip(stack(2, 1, 0), stack("two", "one", "zero"))
			.assertEqualTo(stack(2 to "two", 1 to "one", 0 to "zero"))
		leo13.zip(stack(2, 1, 0), stack("one", "zero"))
			.assertEqualTo(stack(2 to null, 1 to "one", 0 to "zero"))
		leo13.zip(stack(1, 0), stack("two", "one", "zero"))
			.assertEqualTo(stack(null to "two", 1 to "one", 0 to "zero"))
	}

	@Test
	fun map() {
		stack(1, 2, 3).map { inc() }.assertEqualTo(stack(2, 3, 4))
	}

	@Test
	fun flatMap() {
		stack(stack(1, 2), stack(3, 4))
			.flatMap { push(0) }
			.assertEqualTo(stack(1, 2, 0, 3, 4, 0))
	}

	@Test
	fun deduplicate() {
		stack<Int>().deduplicate.assertEqualTo(stack())
		stack(1, 2, 3, 2).deduplicate.assertEqualTo(stack(1, 3, 2))
	}

	@Test
	fun containsDistinct() {
		stack<Int>().containsDistinct.assertEqualTo(true)
		stack(1, 2).containsDistinct.assertEqualTo(true)
		stack(1, 2, 1).containsDistinct.assertEqualTo(false)
	}

	@Test
	fun indexing() {
		val stack = stack("zero", "one", "two")

		stack.firstIndexed { this == "zero" }.assertEqualTo(0 indexed "zero")
		stack.firstIndexed { this == "one" }.assertEqualTo(1 indexed "one")
		stack.firstIndexed { this == "two" }.assertEqualTo(2 indexed "two")
		stack.firstIndexed { this == "three" }.assertEqualTo(null)

		stack.atIndex(0).assertEqualTo("zero")
		stack.atIndex(1).assertEqualTo("one")
		stack.atIndex(2).assertEqualTo("two")
		stack.atIndex(3).assertEqualTo(null)
	}

	@Test
	fun pushOrNull() {
		stack(1, 2).reversePushOrNull(stack(4, 3), 0).assertEqualTo(stack(1, 2))
		stack(1, 2).reversePushOrNull(stack(4, 3), 1).assertEqualTo(stack(1, 2, 3))
		stack(1, 2).reversePushOrNull(stack(4, 3), 2).assertEqualTo(stack(1, 2, 3, 4))
		stack(1, 2).reversePushOrNull(stack(4, 3), 3).assertEqualTo(null)
	}

	@Test
	fun takeOrNull() {
		stack(1, 2).takeOrNull(0).assertEqualTo(stack())
		stack(1, 2).takeOrNull(1).assertEqualTo(stack(2))
		stack(1, 2).takeOrNull(2).assertEqualTo(stack(1, 2))
		stack(1, 2).takeOrNull(3).assertEqualTo(null)
	}
}