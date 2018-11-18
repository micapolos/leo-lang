package leo.lab

import leo.bWord
import leo.base.assertEqualTo
import leo.base.bitByteStreamOrNull
import leo.base.stack
import leo.base.utf8string
import leo.cWord
import leo.oneWord
import leo.twoWord
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
					Scope(identityFunction, null),
					bWord),
				TokenEvaluator.Entry(
					Scope(identityFunction, oneWord.term),
					twoWord)),
			Scope(identityFunction, cWord.term))
			.bitStreamOrNull
			?.bitByteStreamOrNull
			.utf8string
			.assertEqualTo("b(one()two(c()")
	}
}