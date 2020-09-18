package vm.c

import leo.base.assertEqualTo
import vm.of
import vm.value
import vm.get
import kotlin.test.Test

class ProgramTest {
	@Test
	fun program() {
		val point = value(
			"x" of value(10),
			"y" of value(10))
		val circle = value(
			"radius" of value(10),
			"center" of value(
				"point" of point))
		val area = circle["center"]["point"]["x"]
		area.bodyCode.clangFormat.assertEqualTo("")
	}
}