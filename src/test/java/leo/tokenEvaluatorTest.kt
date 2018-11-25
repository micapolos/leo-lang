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
	fun evaluate_word_begin_end() {
		emptyTokenEvaluator
			.evaluate(oneWord.token)!!
			.evaluate(begin.control.token)!!
			.evaluate(end.control.token)
			.assertEqualTo(
				TokenEvaluator(
					entryStackOrNull = null,
					wordOrNull = null,
					scope = Scope(
						function = emptyFunction,
						termOrNull = oneWord.term)))
	}

	@Test
	fun evaluate_word_begin_word() {
		emptyTokenEvaluator
			.evaluate(oneWord.token)!!
			.evaluate(begin.control.token)!!
			.evaluate(numberWord.token)
			.assertEqualTo(
				TokenEvaluator(
					entryStackOrNull = stack(
						TokenEvaluator.Entry(
							scope = Scope(
								function = emptyFunction,
								termOrNull = null),
							word = oneWord)),
					wordOrNull = numberWord,
					scope = Scope(
						function = emptyFunction,
						termOrNull = null)))
	}

	@Test
	fun evaluate_word_begin_word_end() {
		emptyTokenEvaluator
			.evaluate(oneWord.token)!!
			.evaluate(begin.control.token)!!
			.evaluate(numberWord.token)!!
			.evaluate(end.control.token)
			.assertEqualTo(null)
	}

	@Test
	fun evaluate_word_begin_word_word() {
		emptyTokenEvaluator
			.evaluate(oneWord.token)!!
			.evaluate(begin.control.token)!!
			.evaluate(numberWord.token)!!
			.evaluate(oneWord.token)
			.assertEqualTo(null)
	}

	@Test
	fun evaluate_word_begin_word_begin() {
		emptyTokenEvaluator
			.evaluate(oneWord.token)!!
			.evaluate(begin.control.token)!!
			.evaluate(numberWord.token)!!
			.evaluate(begin.control.token)
			.assertEqualTo(
				TokenEvaluator(
					entryStackOrNull = stack(
						TokenEvaluator.Entry(
							scope = Scope(
								function = emptyFunction,
								termOrNull = null),
							word = oneWord),
						TokenEvaluator.Entry(
							scope = Scope(
								function = emptyFunction,
								termOrNull = null),
							word = numberWord)),
					wordOrNull = null,
					scope = Scope(
						function = emptyFunction,
						termOrNull = null)))
	}

	@Test
	fun evaluate_word_begin_word_begin_end() {
		emptyTokenEvaluator
			.evaluate(oneWord.token)!!
			.evaluate(begin.control.token)!!
			.evaluate(numberWord.token)!!
			.evaluate(begin.control.token)!!
			.evaluate(end.control.token)
			.assertEqualTo(
				TokenEvaluator(
					entryStackOrNull = stack(
						TokenEvaluator.Entry(
							scope = Scope(
								function = emptyFunction,
								termOrNull = null),
							word = oneWord)),
					wordOrNull = null,
					scope = Scope(
						function = emptyFunction,
						termOrNull = numberWord.term)))
	}

	@Test
	fun evaluate_word_begin_word_begin_end_end() {
		emptyTokenEvaluator
			.evaluate(oneWord.token)!!
			.evaluate(begin.control.token)!!
			.evaluate(numberWord.token)!!
			.evaluate(begin.control.token)!!
			.evaluate(end.control.token)!!
			.evaluate(end.control.token)
			.assertEqualTo(
				TokenEvaluator(
					entryStackOrNull = null,
					wordOrNull = null,
					scope = Scope(
						function = emptyFunction,
						termOrNull = term(oneWord fieldTo numberWord.term))))
	}
}