package leo14.lambda.evaluator

import leo.base.assertEqualTo
import leo14.lambda.eitherFirst
import leo14.lambda.eitherSecond
import leo14.lambda.eitherSwitch
import leo14.lambda.first
import leo14.lambda.fn
import leo14.lambda.pair
import leo14.lambda.second
import leo14.lambda.term
import kotlin.test.Test

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
}