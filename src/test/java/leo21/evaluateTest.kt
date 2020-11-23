package leo21

import leo.base.assertEqualTo
import leo15.dsl.*
import kotlin.test.Test

class EvaluateTest {
	@Test
	fun numberPlusNumber() {
		script_ {
			number(10)
			plus { number(20) }
		}.evaluate.assertEqualTo(script_ { number(30) })
	}
}