package leo21.token.processor

import leo15.dsl.*
import kotlin.test.Test

class EvaluateTest {
	@Test
	fun define() {
		processor {
			define {
				function {
					x { number }
					y { number }
					does { x.number.plus { y.number } }
				}
			}
			x { number(10) }
			z { number(20) }
		}
	}
}