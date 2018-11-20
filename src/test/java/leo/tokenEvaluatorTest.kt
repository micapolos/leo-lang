package leo

import leo.base.assertEqualTo
import leo.base.bitByteStreamOrNull
import leo.base.stack
import leo.base.utf8string
import kotlin.test.Test

class TokenEvaluatorTest {
	@Test
	fun byteStream_empty() {
		emptyTokenEvaluator
			.bitStreamOrNull
			?.bitByteStreamOrNull
			.utf8string
			.assertEqualTo("")
	}

	@Test
	fun byteStream_a__b_() {
		TokenEvaluator(
			stack(
				TokenEvaluator.Entry(
					Scope(emptyFunction, null),
					bWord),
				TokenEvaluator.Entry(
					Scope(emptyFunction, oneWord.term),
					twoWord)),
			Scope(emptyFunction, cWord.term))
			.bitStreamOrNull
			?.bitByteStreamOrNull
			.utf8string
			.assertEqualTo("b(one()two(c()")
	}
}