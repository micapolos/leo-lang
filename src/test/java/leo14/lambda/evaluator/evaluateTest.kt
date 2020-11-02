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
}