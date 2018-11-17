package leo.base

import kotlin.test.Test

class StreamTest {
	@Test
	fun stackStream() {
		val stream1 = stream(1, 2, 3)
		stream1.first.assertEqualTo(1)
		val stream2 = stream1.nextOrNull!!
		stream2.first.assertEqualTo(2)
		val stream3 = stream2.nextOrNull!!
		stream3.first.assertEqualTo(3)
		stream3.nextOrNull.assertEqualTo(null)
	}

	@Test
	fun fold() {
		stream('a', 'b', 'c')
			.foldFirst(Char::toString)
			.foldNext(String::plus)
			.assertEqualTo("abc")
	}

	@Test
	fun stack() {
		stream(1, 2, 3)
			.stack
			.assertEqualTo(stack(1, 2, 3))
	}

	@Test
	fun map() {
		stream(1, 2, 3)
			.map(Int::toString)
			.assertContains("1", "2", "3")
	}

	@Test
	fun mapNotNull() {
		stream(1, 2, 3, 4)
			.mapNotNull { int -> if (int % 2 == 0) int.toString() else null }
			?.assertContains("2", "4")
	}

	@Test
	fun then() {
		stream(1, 2, 3)
			.then { stream(4, 5, 6) }
			.assertContains(1, 2, 3, 4, 5, 6)
	}

	@Test
	fun join() {
		stream(stream(1, 2, 3), stream(4, 5, 6), stream(7, 8, 9))
			.join
			.assertContains(1, 2, 3, 4, 5, 6, 7, 8, 9)
	}
}

fun <V> Stream<V>.assertContains(first: V, vararg next: V) =
	stack.assertEqualTo(stack(first, *next))