package leo16

import leo.base.iterate
import leo.base.timeMillis
import leo15.dsl.*
import kotlin.math.min
import kotlin.test.Test

fun measure(label: String, fn: () -> Unit) {
	val warmUpTimes = 1
	val measureTimes = 3
	repeat(warmUpTimes) { fn() }
	val time = Long.MAX_VALUE.iterate(measureTimes) { min(this, timeMillis(fn)) }
	println("$label: ${time}ms")
}

class PerformanceTest {
	/**
	 * 05.05.2020: ~141ms
	 * 20.05.2020: ~230ms
	 * 28.05.2020: ~430ms
	 * 30.05.2020: ~480ms
	 */
	@Test
	fun fibonacci() {
		measure("fibonacci") {
			evaluate_ {
				use { boolean }
				use { number }

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