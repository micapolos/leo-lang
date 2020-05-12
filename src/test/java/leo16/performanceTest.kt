package leo16

import leo.base.printTime
import leo15.dsl.*
import kotlin.test.Test

class PerformanceTest {
	/** Last measurement: ~140ms */
	@Test
	fun fibonacci() {
		repeat(15) {
			printTime {
				evaluate_ {
					import { boolean }
					import { number }

					15.number
					do_ {
						number.equals_ { 0.number }
						or { number.equals_ { 1.number } }
						match {
							true_ { number }
							false_ {
								number.minus { 2.number }.repeat
								plus { number.minus { 1.number }.repeat }
							}
						}
					}
				}.assertEquals { 610.number }
			}
		}
	}
}