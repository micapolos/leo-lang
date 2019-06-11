package leo5

import leo.base.assertEqualTo
import leo.base.empty
import org.junit.Test

class ArrayTest {
	@Test
	fun contains() {
		array(cell(type(empty)), size(0))
			.contains(value())
			.assertEqualTo(true)

		array(cell(type(empty)), size(0))
			.contains(value("cell" lineTo value()))
			.assertEqualTo(false)

		array(cell(type(empty)), size(2))
			.contains(value("cell" lineTo value(), "cell" lineTo value()))
			.assertEqualTo(true)

		array(cell(type(empty)), size(2))
			.contains(value("cell" lineTo value()))
			.assertEqualTo(false)
	}
}