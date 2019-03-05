package leo.binary

import leo.base.assertEqualTo
import kotlin.test.Test

class ArrayTest {
	@Test
	fun putAndGet() {
		0.array5
			.forEachInt5 { index -> apply { at(index).assertEqualTo(0) } }
			.forEachInt5 { index -> put(index, index.int) }
			.forEachInt5 { index -> apply { at(index).assertEqualTo(index.int) } }
	}
}