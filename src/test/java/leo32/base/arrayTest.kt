package leo32.base

import leo.base.assertEqualTo
import leo.base.fold
import leo.base.string
import kotlin.test.Test

class ArrayTest {
	private val range: Iterable<Int> = 0 until 65536

	@Test
	fun string() {
		0.array.string.assertEqualTo("0.array")
		0.array.put(1, 128).put(5, 129).string.assertEqualTo("0.array.put(1, 128).put(5, 129)")
	}

	@Test
	fun array() {
		0.array.put(4, 128).at(3).assertEqualTo(0)
		0.array.put(4, 128).at(4).assertEqualTo(128)
		0.array.put(4, 128).at(5).assertEqualTo(0)
	}

	@Test
	fun all() {
		0.array
			.fold(range) { index -> apply { at(index).assertEqualTo(0) } }
			.fold(range) { index -> put(index, index) }
			.fold(range) { index -> apply { at(index).assertEqualTo(index) } }
	}
}