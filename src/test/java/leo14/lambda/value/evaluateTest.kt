package leo14.lambda.value

import leo.base.assertEqualTo
import leo.base.iterate
import leo14.lambda.arg
import leo14.lambda.eitherFirst
import leo14.lambda.eitherFirst2
import leo14.lambda.eitherSecond
import leo14.lambda.eitherSecond2
import leo14.lambda.eitherSwitch
import leo14.lambda.first
import leo14.lambda.fix
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
			.value
			.assertEqualTo(value("foo"))
	}

	@Test
	fun pairSecond() {
		pair(term("foo"), term("bar"))
			.second
			.value
			.assertEqualTo(value("bar"))
	}

	@Test
	fun pairUnpair() {
		pair(term("foo"), term("bar"))
			.value
			.pair { lhs, rhs ->
				lhs.assertEqualTo(value("foo"))
				rhs.assertEqualTo(value("bar"))
			}
	}

	@Test
	fun eitherFirst() {
		term("foo")
			.eitherFirst
			.eitherSwitch(fn(term("first")), fn(term("second")))
			.value
			.assertEqualTo(value("first"))
	}

	@Test
	fun eitherSecond() {
		term("foo")
			.eitherSecond
			.eitherSwitch(fn(term("first")), fn(term("second")))
			.value
			.assertEqualTo(value("second"))
	}

	@Test
	fun eitherFirst2() {
		term("foo")
			.eitherFirst2
			.eitherSwitch(fn(term("first")), fn(term("second")))
			.value
			.assertEqualTo(value("first"))
	}

	@Test
	fun eitherSecond2() {
		term("foo")
			.eitherSecond2
			.eitherSwitch(fn(term("first")), fn(term("second")))
			.value
			.assertEqualTo(value("second"))
	}

	@Test
	fun nativeApply() {
		nativeTerm(Any::anyIntInc)
			.invoke(nativeTerm(1))
			.value
			.assertEqualTo(value(2))
	}

	@Test
	fun tailRecursion() {
		val times = 1000
		fn(arg<Any>(0))
			.iterate(times) { fn(invoke(nativeTerm(Any::anyIntInc).invoke(arg(0)))) }
			.invoke(nativeTerm(0))
			.value
			.assertEqualTo(value(times))
	}

	@Test
	fun fixFib() {
		fix<Any>()
			.invoke(fn(fn(nativeTerm(10))))
			.value
			.function
			.bodyTerm
			.assertEqualTo(nativeTerm(10))
	}
}