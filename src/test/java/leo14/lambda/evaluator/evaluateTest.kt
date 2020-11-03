package leo14.lambda.evaluator

import leo.base.assertEqualTo
import leo.base.assertStackOverflows
import leo.base.iterate
import leo14.lambda.arg
import leo14.lambda.eitherFirst
import leo14.lambda.eitherSecond
import leo14.lambda.eitherSwitch
import leo14.lambda.first
import leo14.lambda.fn
import leo14.lambda.id
import leo14.lambda.invoke
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

	@Test
	fun tailRecursion() {
		// FIXIT: This should not overflow when tail recursion is working.
		assertStackOverflows {
			val times = 100000
			term("done")
				.iterate(times) { fn(this) }
				.iterate(times) { invoke(term("run")) }
				.evaluate
				.assertEqualTo(term("done"))
		}
	}
}