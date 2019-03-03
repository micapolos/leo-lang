package leo.binary

import leo.base.assertEqualTo
import kotlin.test.Test

class ArrayTest {
	@Test
	fun putAndGet() {
		var array = (null as Int?).array5
		for (index in 0..31) array.at(index.wrappingInt5).assertEqualTo(null)
		for (index in 0..31) array = array.put(index.wrappingInt5, index)
		for (index in 0..31) array.at(index.wrappingInt5).assertEqualTo(index)
	}
}