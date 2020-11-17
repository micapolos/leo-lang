package leo21.token.processor

import leo15.dsl.*
import kotlin.test.Test

class EvaluateTest {
	@Test
	fun define() {
		processor {
			define {
				function {
					it { number.negate }
					does { number(0).minus { number } }
				}
				function {
					it { number.square }
					does { number.times { number } }
				}
				function {
					it { number.double }
					does { number.plus { number } }
				}
			}
			number(123).square
		}
	}
}