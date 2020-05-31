package leo15.lambda

import leo.base.assertEqualTo
import leo.base.printTime
import leo15.terms.ifIntNegative
import leo15.terms.intMinus
import leo15.terms.intPlus
import leo15.terms.otherwise
import leo15.terms.term
import kotlin.test.Test

class PerformanceTest {
	@Test
	fun fib() {
		repeat(10) {
			printTime {
				lambda { f ->
					lambda { x ->
						f.invoke(f).invoke(x)
					}
				}.invoke(
					lambda { f ->
						lambda { x ->
							x.intMinus(2.term)
								.ifIntNegative { x }
								.otherwise {
									f.invoke(f).invoke(x.intMinus(1.term))
										.intPlus(f.invoke(f).invoke(x.intMinus(2.term)))
								}
						}
					})
					.invoke(15.term)
					.eval
					.assertEqualTo(610.term)
			}
		}
	}
}