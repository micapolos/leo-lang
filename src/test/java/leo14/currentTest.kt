package leo14

import leo.base.assertEqualTo
import kotlin.test.Test

class CurrentTest {
	private val currentString = "root".current

	@Test
	fun set() {
		currentString().assertEqualTo("root")
		currentString.with("level1") {
			currentString().assertEqualTo("level1")
			currentString.with("level2") {
				currentString().assertEqualTo("level2")
			}
			currentString.with("level3") {
				currentString().assertEqualTo("level3")
			}
			currentString().assertEqualTo("level1")
		}
		currentString().assertEqualTo("root")
	}
}