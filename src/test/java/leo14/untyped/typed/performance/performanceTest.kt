package leo14.untyped.typed.performance

import leo.base.assertEqualTo
import leo.base.iterate
import leo.base.printTime
import leo14.Number
import leo14.lambda.runtime.Value
import leo14.number
import leo14.plus
import leo14.untyped.typed.*
import kotlin.test.Test

class PerformanceTest {
	@Test
	fun performance() {
		val nativeFn: Fn = { x -> (x as Number).plus(number(1)) }
		val compiledFn: (Value) -> Value = "number".valueTypedFunction {
			eval(it, "plus"(number(1).typed))
		}.value as Fn

		val times = 1000001
		val repetitions = 10
		repeat(repetitions) {
			printTime("Native: ") {
				(number(0) as Value).iterate(times) { nativeFn(this) }.assertEqualTo(number(times))
			}

			printTime("Leo: ") {
				(number(0) as Value).iterate(times) { compiledFn(this) }.assertEqualTo(number(times))
			}
		}
	}
}