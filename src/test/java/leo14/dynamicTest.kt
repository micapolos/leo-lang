package leo14

import leo.base.assertEqualTo
import kotlin.test.Test

class DynamicTest {
	private val dynamicString = "root".dynamic

	@Test
	fun set() {
		dynamicString().assertEqualTo("root")
		dynamicString.with("level1") {
			dynamicString().assertEqualTo("level1")
			dynamicString.with("level2") {
				dynamicString().assertEqualTo("level2")
			}
			dynamicString.with("level3") {
				dynamicString().assertEqualTo("level3")
			}
			dynamicString().assertEqualTo("level1")
		}
		dynamicString().assertEqualTo("root")
	}
}