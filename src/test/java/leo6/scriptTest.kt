package leo6

import leo.base.assertEqualTo
import kotlin.test.Test

class ScriptTest {
	@Test
	fun construction() {
		script()
		script("foo")
		script("foo" lineTo script())
		script("foo" lineTo script(), "bar" lineTo script())
	}

	@Test
	fun scriptPath() {
		script().pathOrNull.assertEqualTo(path())
		script("one").pathOrNull.assertEqualTo(path("one"))
		script("one" lineTo script("two")).pathOrNull.assertEqualTo(path("one", "two"))
	}

	@Test
	fun containsPath() {
		val circle = script(
			"circle" lineTo script(
				"radius" lineTo "12",
				"center" lineTo script(
					"x" lineTo "13",
					"y" lineTo "14")))

		circle.contains(path("circle")).assertEqualTo(true)
		circle.contains(path("circle", "radius")).assertEqualTo(true)
		circle.contains(path("circle", "radius", "12")).assertEqualTo(true)
		circle.contains(path("circle", "radius", "13")).assertEqualTo(false)
		circle.contains(path("circle", "center")).assertEqualTo(true)
		circle.contains(path("circle", "center", "x")).assertEqualTo(true)
		circle.contains(path("circle", "center", "y")).assertEqualTo(true)
	}
}