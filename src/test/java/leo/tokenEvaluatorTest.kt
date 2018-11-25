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
			Scope(emptyFunction, cWord.term),
			null)
			.bitStreamOrNull
			?.bitByteStreamOrNull
			.utf8string
			.assertEqualTo("b(it(one())two(c()")
	}

	@Test
	fun evaluate_begin() {
		emptyTokenEvaluator
			.evaluate(begin.control.token)
			.assertEqualTo(null)
	}

	@Test
	fun evaluate_end() {
		emptyTokenEvaluator
			.evaluate(end.control.token)
			.assertEqualTo(null)
	}

	@Test
	fun evaluate_word() {
		emptyTokenEvaluator
			.evaluate(oneWord.token)
			.assertEqualTo(
				TokenEvaluator(
					entryStackOrNull = null,
					scope = Scope(
						function = emptyFunction,
						termOrNull = null),
					wordOrNull = oneWord))
	}

	@Test
	fun evaluate_word_begin() {
		emptyTokenEvaluator
			.evaluate(oneWord.token)!!
			.evaluate(begin.control.token)
			.assertEqualTo(
				TokenEvaluator(
					entryStackOrNull = stack(
						TokenEvaluator.Entry(
							scope = Scope(
								function = emptyFunction,
								termOrNull = null),
							word = oneWord)),
					scope = Scope(
						function = emptyFunction,
						termOrNull = null),
					wordOrNull = null))
	}

	@Test
	fun evaluate_word_end() {
		emptyTokenEvaluator
			.evaluate(oneWord.token)!!
			.evaluate(end.control.token)
			.assertEqualTo(null)
	}

	@Test
	fun evaluate_word_word() {
		emptyTokenEvaluator
			.evaluate(oneWord.token)!!
			.evaluate(twoWord.token)
			.assertEqualTo(null)
	}

	@Test
	fun evaluate_word_begin_word() {
		emptyTokenEvaluator
			.evaluate(oneWord.token)!!
			.evaluate(begin.control.token)!!
			.evaluate(twoWord.token)
			.assertEqualTo(
				TokenEvaluator(
					entryStackOrNull = stack(
						TokenEvaluator.Entry(
							scope = Scope(
								function = emptyFunction,
								termOrNull = null),
							word = oneWord)),
					scope = Scope(
						function = emptyFunction,
						termOrNull = null),
					wordOrNull = twoWord))
	}

//	@Test
//	fun evaluate_word_begin_word_end() {
//		emptyTokenEvaluator
//			.evaluate(oneWord.token)!!
//			.evaluate(begin.control.token)!!
//			.evaluate(twoWord.token)!!
//			.evaluate(end.control.token)
//			.assertEqualTo(
//				TokenEvaluator(
//					entryStackOrNull = null,
//					scope = Scope(
//						function = emptyFunction,
//						termOrNull = term(oneWord fieldTo twoWord.term)),
//					wordOrNull = null))
//	}
}