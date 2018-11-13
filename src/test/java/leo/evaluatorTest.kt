package leo

import leo.base.assertEqualTo
import leo.base.stack
import kotlin.test.Test

class EvaluatorTest {
	@Test
	fun evaluator() {
		emptyEvaluator
			.assertEqualTo(
				Evaluator(
					scopeStack = stack(
						Scope(
							parentWord = evaluateWord,
							function = identityFunction,
							valueTermOrNull = null)),
					wordOrNull = null,
					readerValueTerm = leoReaderTerm()))
	}

	@Test
	fun push_one() {
		emptyEvaluator
			.push(oneWord)
			.assertEqualTo(
				Evaluator(
					scopeStack = stack(
						Scope(
							parentWord = evaluateWord,
							function = identityFunction,
							valueTermOrNull = null)),
					wordOrNull = oneWord,
					readerValueTerm = leoReaderTerm()))
	}

	@Test
	fun push_one_() {
		emptyEvaluator
			.push(oneWord)
			?.begin
			.assertEqualTo(
				Evaluator(
					scopeStack = stack(
						Scope(
							parentWord = evaluateWord,
							function = identityFunction,
							valueTermOrNull = null),
						Scope(
							parentWord = oneWord,
							function = identityFunction,
							valueTermOrNull = null)),
					wordOrNull = null,
					readerValueTerm = leoReaderTerm()))
	}

	@Test
	fun push_one_number() {
		emptyEvaluator
			.push(oneWord)
			?.begin
			?.push(numberWord)
			.assertEqualTo(
				Evaluator(
					scopeStack = stack(
						Scope(
							parentWord = evaluateWord,
							function = identityFunction,
							valueTermOrNull = null),
						Scope(
							parentWord = oneWord,
							function = identityFunction,
							valueTermOrNull = null)),
					wordOrNull = numberWord,
					readerValueTerm = leoReaderTerm()))
	}

	@Test
	fun push_one_number__() {
		emptyEvaluator
			.push(oneWord)
			?.begin
			?.push(numberWord)
			?.end
			.assertEqualTo(
				Evaluator(
					scopeStack = stack(
						Scope(
							parentWord = evaluateWord,
							function = identityFunction,
							valueTermOrNull = term(oneWord fieldTo term(numberWord)))),
					wordOrNull = null,
					readerValueTerm = leoReaderTerm()))
	}

	@Test
	fun push_one_number__two() {
		emptyEvaluator
			.push(oneWord)
			?.begin
			?.push(numberWord)
			?.end
			?.push(twoWord)
			.assertEqualTo(
				Evaluator(
					scopeStack = stack(
						Scope(
							parentWord = evaluateWord,
							function = identityFunction,
							valueTermOrNull = term(oneWord fieldTo term(numberWord)))),
					wordOrNull = twoWord,
					readerValueTerm = leoReaderTerm()))
	}

	@Test
	fun push_one_number__two_() {
		emptyEvaluator
			.push(oneWord)
			?.begin
			?.push(numberWord)
			?.end
			?.push(twoWord)
			?.begin
			.assertEqualTo(
				Evaluator(
					scopeStack = stack(
						Scope(
							parentWord = evaluateWord,
							function = identityFunction,
							valueTermOrNull = term(oneWord fieldTo term(numberWord))),
						Scope(
							parentWord = twoWord,
							function = identityFunction,
							valueTermOrNull = null)),
					wordOrNull = null,
					readerValueTerm = leoReaderTerm()))
	}

	@Test
	fun push_one_number__two_string() {
		emptyEvaluator
			.push(oneWord)
			?.begin
			?.push(numberWord)
			?.end
			?.push(twoWord)
			?.begin
			?.push(stringWord)
			.assertEqualTo(
				Evaluator(
					scopeStack = stack(
						Scope(
							parentWord = evaluateWord,
							function = identityFunction,
							valueTermOrNull = term(oneWord fieldTo term(numberWord))),
						Scope(
							parentWord = twoWord,
							function = identityFunction,
							valueTermOrNull = null)),
					wordOrNull = stringWord,
					readerValueTerm = leoReaderTerm()))
	}


	@Test
	fun push_one_number__two_string__() {
		emptyEvaluator
			.push(oneWord)
			?.begin
			?.push(numberWord)
			?.end
			?.push(twoWord)
			?.begin
			?.push(stringWord)
			?.end
			.assertEqualTo(
				Evaluator(
					scopeStack = stack(
						Scope(
							parentWord = evaluateWord,
							function = identityFunction,
							valueTermOrNull = term(
								oneWord fieldTo term(numberWord),
								twoWord fieldTo term(stringWord)))),
					wordOrNull = null,
					readerValueTerm = leoReaderTerm()))
	}

	@Test
	fun push_define_it_one__is_number___() {
		emptyEvaluator
			.push(defineWord)
			?.begin
			?.push(itWord)
			?.begin
			?.push(oneWord)
			?.end
			?.push(isWord)
			?.begin
			?.push(numberWord)
			?.end
			?.end
			.assertEqualTo(
				Evaluator(
					scopeStack = stack(
						Scope(
							parentWord = evaluateWord,
							function = Function(
								stack(
									term<Pattern>(oneWord) returns
										Body(term(numberWord), identityFunction))),
							valueTermOrNull = null)),
					wordOrNull = null,
					readerValueTerm = leoReaderTerm()))
	}

	@Test
	fun push_define_it_one__is_number___it() {
		emptyEvaluator
			.push(defineWord)
			?.begin
			?.push(itWord)
			?.begin
			?.push(oneWord)
			?.end
			?.push(isWord)
			?.begin
			?.push(numberWord)
			?.end
			?.end
			?.push(itWord)
			.assertEqualTo(
				Evaluator(
					scopeStack = stack(
						Scope(
							parentWord = evaluateWord,
							function = Function(
								stack(
									term<Pattern>(oneWord) returns
										body(
											term(numberWord),
											identityFunction))),
							valueTermOrNull = null)),
					wordOrNull = itWord,
					readerValueTerm = leoReaderTerm()))
	}

	@Test
	fun push_define_it_one__is_number___it_() {
		emptyEvaluator
			.push(defineWord)
			?.begin
			?.push(itWord)
			?.begin
			?.push(oneWord)
			?.end
			?.push(isWord)
			?.begin
			?.push(numberWord)
			?.end
			?.end
			?.push(itWord)
			?.begin
			.assertEqualTo(
				Evaluator(
					scopeStack = stack(
						Scope(
							parentWord = evaluateWord,
							function = Function(
								stack(
									term<Pattern>(oneWord) returns
										body(
											term(numberWord),
											identityFunction))),
							valueTermOrNull = null),
						Scope(
							parentWord = itWord,
							function = Function(
								stack(
									term<Pattern>(oneWord) returns
										body(
											term(numberWord),
											identityFunction))),
							valueTermOrNull = null)),
					wordOrNull = null,
					readerValueTerm = leoReaderTerm()))
	}

	@Test
	fun push_define_it_one__is_number___it_one() {
		emptyEvaluator
			.push(defineWord)
			?.begin
			?.push(itWord)
			?.begin
			?.push(oneWord)
			?.end
			?.push(isWord)
			?.begin
			?.push(numberWord)
			?.end
			?.end
			?.push(itWord)
			?.begin
			?.push(oneWord)
			.assertEqualTo(
				Evaluator(
					scopeStack = stack(
						Scope(
							parentWord = evaluateWord,
							function = Function(
								stack(
									term<Pattern>(oneWord) returns
										body(
											term(numberWord),
											identityFunction))),
							valueTermOrNull = null),
						Scope(
							parentWord = itWord,
							function = Function(
								stack(
									term<Pattern>(oneWord) returns
										body(
											term(numberWord),
											identityFunction))),
							valueTermOrNull = null)),
					wordOrNull = oneWord,
					readerValueTerm = leoReaderTerm()
				)
			)
	}

	@Test
	fun push_define_it_one__is_number___it_one__() {
		emptyEvaluator
			.push(defineWord)
			?.begin
			?.push(itWord)
			?.begin
			?.push(oneWord)
			?.end
			?.push(isWord)
			?.begin
			?.push(numberWord)
			?.end
			?.end
			?.push(itWord)
			?.begin
			?.push(oneWord)
			?.end
			.assertEqualTo(
				Evaluator(
					scopeStack = stack(
						Scope(
							parentWord = evaluateWord,
							function = Function(
								stack(
									term<Pattern>(oneWord) returns
										body(
											term(numberWord),
											identityFunction))),
							valueTermOrNull = term(itWord fieldTo term(numberWord)))),
					wordOrNull = null,
					readerValueTerm = leoReaderTerm()))
	}

	@Test
	fun pushValidByte() {
		emptyEvaluator
			.push(97.toByte())
			.assertEqualTo(
				Evaluator(
					scopeStack = stack(
						Scope(
							parentWord = evaluateWord,
							function = identityFunction,
							valueTermOrNull = null)),
					wordOrNull = stack(Letter.A).word,
					readerValueTerm = leoReaderTerm()))
	}
}