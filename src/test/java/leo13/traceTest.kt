package leo13

import leo13.script.lineTo
import leo13.script.script
import kotlin.test.Test

class TraceTest {
	@Test
	fun tracing() {
		traced {
			trace { "step" lineTo script("zero") }.that {
				trace { "step" lineTo script("one") }.that {
					tracedError<Integer>()
				}
			}
		}.assertFailsWith(
			"step" lineTo script("zero"),
			"step" lineTo script("one"))
	}
}