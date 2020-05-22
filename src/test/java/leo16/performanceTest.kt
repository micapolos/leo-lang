package leo16

import leo.base.iterate
import leo.base.timeMillis
import leo15.dsl.*
import kotlin.test.Test

fun measure(label: String, fn: () -> Unit) {
	val warmUpTimes = 5
	val measureTimes = 10
	repeat(warmUpTimes) { fn() }
	val time = 0L.iterate(measureTimes) { plus(timeMillis(fn)) }.div(measureTimes)
	println("$label: ${time}ms")
}

class PerformanceTest {
	/**
	 * 05.05.2020: ~141ms
	 * 20.05.2020: ~230ms
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