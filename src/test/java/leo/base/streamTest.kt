package leo.base

import kotlin.test.Test

class StreamTest {
	@Test
	fun stackStream() {
		val stream1 = stack(1, 2, 3).stream
		stream1.first.assertEqualTo(3)
		val stream2 = stream1.next!!
		stream2.first.assertEqualTo(2)
		val stream3 = stream2.next!!
		stream3.first.assertEqualTo(1)
		stream3.next.assertEqualTo(null)
	}

	@Test
	fun fold() {
		stack('a', 'b', 'c')
			.stream
			.foldFirst(Char::toString)
			.foldNext(String::plus)
			.assertEqualTo("cba")
	}

	@Test
	fun reversedStack() {
		stack(1, 2, 3)
			.stream
			.reversedStack
			.assertEqualTo(stack(3, 2, 1))
	}
}