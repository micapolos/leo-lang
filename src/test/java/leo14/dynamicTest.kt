package leo14

import leo.base.assertEqualTo
import kotlin.test.Test

class DynamicTest {
	private val dynamicString = "root".dynamic

	@Test
	fun set() {
		dynamicString.current.assertEqualTo("root")
		dynamicString.set("level1") {
			dynamicString.current.assertEqualTo("level1")
			dynamicString.set("level2") {
				dynamicString.current.assertEqualTo("level2")
			}
			dynamicString.set("level3") {
				dynamicString.current.assertEqualTo("level3")
			}
			dynamicString.current.assertEqualTo("level1")
		}
		dynamicString.current.assertEqualTo("root")
	}
}