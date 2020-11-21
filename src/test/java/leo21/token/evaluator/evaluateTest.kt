package leo21.token.evaluator

import leo.base.assertEqualTo
import leo15.dsl.*
import leo21.evaluator.evaluated
import kotlin.test.Test

class EvaluateTest {
	@Test
	fun numberPlusNumber() {
		evaluated {
			number(10)
			plus { number(20) }
		}.assertEqualTo(evaluated(30.0))
	}

	@Test
	fun function() {
		evaluated {
			function {
				number
				does { text("ok") }
			}
		}.assertEqualTo(evaluated())
	}

	@Test
	fun functionApply() {
		evaluated {
			function {
				number
				does { text("ok") }
			}
			apply { number(123) }
		}.assertEqualTo(evaluated("ok"))
	}

	@Test
	fun defineFunction() {
		evaluated {
			define {
				function {
					number
					does { text("ok") }
				}
			}
		}.assertEqualTo(null)
	}
}