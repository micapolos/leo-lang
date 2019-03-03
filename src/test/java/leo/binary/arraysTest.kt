package leo.binary

import leo.base.assertEqualTo
import kotlin.test.Test

class ArrayTest {
	@Test
	fun putAndGet() {
		var array = (-1).array5
		array.forEachInt5 { at(it).assertEqualTo(-1); this }
		array = array.forEachInt5 { put(it, it.int) }
		array.forEachInt5 { at(it).assertEqualTo(it.int); this }
	}
}