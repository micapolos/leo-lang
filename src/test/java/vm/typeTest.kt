package vm

import kotlin.test.Test

class TypeTest {
	@Test
	fun dsl() {
		int
		float
		int[10]
		struct(
			"firstName" of string,
			"lastName" of string,
			"age" of int)
		choice(
			"circle" of struct("radius" of float),
			"rectangle" of struct(
				"width" of float,
				"height" of float))
	}
}