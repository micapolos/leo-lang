package leo14.lambda.value

import leo.base.assertEqualTo
import leo.base.iterate
import leo14.lambda.arg
import leo14.lambda.eitherFirst
import leo14.lambda.eitherSecond
import leo14.lambda.eitherSwitch
import leo14.lambda.first
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lambda.nativeTerm
import leo14.lambda.pair
import leo14.lambda.second
import leo14.lambda.term
import kotlin.test.Test

val Any.anyIntInc: Any get() = (this as Int).inc()

class EvaluateTest {
	@Test
	fun pairFirst() {
		pair(term("foo"), term("bar"))
			.first
			.evaluate
			.assertEqualTo(term("foo"))
	}

	@Test
	fun pairSecond() {
		pair(term("foo"), term("bar"))
			.second
			.evaluate
			.assertEqualTo(term("bar"))
	}

	@Test
	fun pairUnpair() {
		pair(term("foo"), term("bar"))
			.evaluate
			.pair()
			.assertEqualTo(term("foo") to term("bar"))
	}

	@Test
	fun eitherFirst() {
		term("foo")
			.eitherFirst
			.eitherSwitch(fn(term("first")), fn(term("second")))
			.evaluate
			.assertEqualTo(term("first"))
	}

	@Test
	fun eitherSecond() {
		term("foo")
			.eitherSecond
			.eitherSwitch(fn(term("first")), fn(term("second")))
			.evaluate
			.assertEqualTo(term("second"))
	}

	@Test
	fun nativeApply() {
		nativeTerm(Any::anyIntInc)
			.invoke(nativeTerm(1))
			.evaluate
			.assertEqualTo(nativeTerm(2))
	}

	@Test
	fun tailRecursion() {
		val times = 1000000
		fn(arg<Any>(0))
			.iterate(times) { fn(invoke(nativeTerm(Any::anyIntInc).invoke(arg(0)))) }
			.invoke(nativeTerm(0))
			.evaluate
			.assertEqualTo(nativeTerm(times))
	}
}