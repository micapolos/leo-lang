package leo13

import leo.base.assertEqualTo
import leo.binary.zero
import kotlin.test.Test

class IndexTest {
	@Test
	fun int() {
		zero.index.int.assertEqualTo(0)
		zero.index.next.int.assertEqualTo(1)
		zero.index.next.next.int.assertEqualTo(2)
	}

	@Test
	fun fromInt() {
		0.index.assertEqualTo(zero.index)
		1.index.assertEqualTo(zero.index.next)
		2.index.assertEqualTo(zero.index.next.next)
	}

	@Test
	fun string() {
		0.index.toString().assertEqualTo("index(0)")
		1.index.toString().assertEqualTo("index(1)")
	}
}