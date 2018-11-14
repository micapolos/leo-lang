package leo.base

import kotlin.test.Test

class StreamTest {
	@Test
	fun stackStream() {
		val stream1 = stream(1, 2, 3)
		stream1.first.assertEqualTo(3)
		val stream2 = stream1.next!!
		stream2.first.assertEqualTo(2)
		val stream3 = stream2.next!!
		stream3.first.assertEqualTo(1)
		stream3.next.assertEqualTo(null)
	}

	@Test
	fun fold() {
		stream('a', 'b', 'c')
			.foldFirst(Char::toString)
			.foldNext(String::plus)
			.assertEqualTo("cba")
	}

	@Test
	fun reversedStack() {
		stream(1, 2, 3)
			.reversedStack
			.assertEqualTo(stack(3, 2, 1))
	}

	@Test
	fun map() {
		stream(1, 2, 3)
			.map(Int::toString)
			.assertContains("1", "2", "3")
	}

	@Test
	fun filterMap() {
		stream(1, 2, 3, 4)
			.filterMap { int -> if (int % 2 == 0) int.toString() else null }
			?.assertContains("2", "4")
	}
}

fun <V> Stream<V>.assertContains(first: V, vararg next: V) =
	stack.assertEqualTo(stack(first, *next))