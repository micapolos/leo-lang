package leo.java.lang.reflect

import leo.base.assertEqualTo
import leo.base.iterate
import leo.base.printTime
import kotlin.test.Test

class PerformanceTest {
	@Test
	fun test() {
		val class_ = java.lang.String::class.java
		val method = class_.getMethod("toUpperCase")
		val times = 100000000

		repeat(10) {
			printTime("Native: ") {
				"Hello".iterate(times) { toUpperCase() }
					.assertEqualTo("HELLO")
			}

			printTime("Reflection: ") {
				"Hello"
					.iterate(times) { method.invoke(this) as String }
					.assertEqualTo("HELLO")
			}
		}
	}
}