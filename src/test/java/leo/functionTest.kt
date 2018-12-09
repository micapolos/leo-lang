package leo

import leo.base.assertEqualTo
import leo.base.back
import kotlin.test.Test

class FunctionTest {
	private val testBody = body(term(invokedWord fieldTo selector().meta.term), emptyFunction)

	private fun Function.testDefine(term: Term<Pattern>): Function? =
		define(rule(term, testBody))

	private fun Function.assertInvokesBody(argument: Term<Nothing>) =
		invoke(argument).assertEqualTo(term(invokedWord fieldTo argument))

	private fun Function.assertDoesNotInvokeBody(argument: Term<Nothing>) =
		invoke(argument).assertEqualTo(argument)

	@Test
	fun defineSimpleAndInvoke_match() {
		emptyFunction
			.testDefine(aWord.term)!!
			.assertInvokesBody(aWord.term)
	}

	@Test
	fun defineSimpleAndInvoke_mismatch() {
		emptyFunction
			.testDefine(aWord.term)!!
			.assertDoesNotInvokeBody(bWord.term)
	}

	@Test
	fun defineTwo_getFirst() {
		emptyFunction
			.testDefine(ageWord.term)!!
			.testDefine(nameWord.term)!!
			.assertInvokesBody(ageWord.term)
	}

	@Test
	fun defineTwo_getSecond() {
		emptyFunction
			.testDefine(ageWord.term)!!
			.testDefine(nameWord.term)!!
			.assertInvokesBody(nameWord.term)
	}

	@Test
	fun defineCommonPrefix_getFirst() {
		emptyFunction
			.testDefine(bodyWord.term)!!
			.testDefine(booleanWord.term)!!
			.assertInvokesBody(bodyWord.term)
	}

	@Test
	fun defineCommonPrefix_getSecond() {
		emptyFunction
			.testDefine(bodyWord.term)!!
			.testDefine(booleanWord.term)!!
			.assertInvokesBody(booleanWord.term)
	}

	@Test
	fun redefineSame() {
		emptyFunction
			.testDefine(aWord.term)!!
			.testDefine(aWord.term)
			.assertEqualTo(null)
	}

	@Test
	fun redefineLonger() {
		emptyFunction
			.testDefine(term(aWord.itField))!!
			.testDefine(term(aWord.itField, bWord.itField))
			.assertEqualTo(null)
	}

	@Test
	fun redefineShorter() {
		emptyFunction
			.testDefine(term(aWord.itField, bWord.itField))!!
			.testDefine(term(aWord.itField))
			.assertEqualTo(null)
	}

	@Test
	fun defineWordAndField_invokeWord() {
		emptyFunction
			.testDefine(aWord.term)!!
			.testDefine(term(aWord fieldTo bWord.term))!!
			.assertInvokesBody(aWord.term)
	}

	@Test
	fun defineWordAndField_invokeField() {
		emptyFunction
			.testDefine(aWord.term)!!
			.testDefine(term(aWord fieldTo bWord.term))!!
			.assertInvokesBody(term(aWord fieldTo bWord.term))
	}

	@Test
	fun defineOneOf_invokeFirst() {
		emptyFunction
			.testDefine(oneOfPattern(aWord.term(), bWord.term()).meta.term)!!
			.assertInvokesBody(aWord.term)
	}

	@Test
	fun defineOneOf_invokeSecond() {
		emptyFunction
			.testDefine(oneOfPattern(aWord.term(), bWord.term()).meta.term)!!
			.assertInvokesBody(bWord.term)
	}

	private val nonDependentPatternsFunction = emptyFunction
		.testDefine(
			term(
				oneWord fieldTo oneOfPattern(aWord.term(), bWord.term()).meta.term,
				twoWord fieldTo oneOfPattern(aWord.term(), bWord.term()).meta.term))

	@Test
	fun defineNonDependentOneOfs_getFirstFirst() {
		nonDependentPatternsFunction!!
			.assertInvokesBody(term(oneWord fieldTo aWord.term, twoWord fieldTo aWord.term))
	}

	@Test
	fun defineNonDependentOneOfs_getFirstSecond() {
		nonDependentPatternsFunction!!
			.assertInvokesBody(term(oneWord fieldTo aWord.term, twoWord fieldTo bWord.term))
	}

	@Test
	fun defineNonDependentOneOfs_getSecondFirst() {
		nonDependentPatternsFunction!!
			.assertInvokesBody(term(oneWord fieldTo bWord.term, twoWord fieldTo aWord.term))
	}

	@Test
	fun defineNonDependentOneOfs_getSecondSecond() {
		nonDependentPatternsFunction!!
			.assertInvokesBody(term(oneWord fieldTo bWord.term, twoWord fieldTo bWord.term))
	}

	private val dependentPatternsFunction = emptyFunction
		.testDefine(
			oneOfPattern(
				term(
					oneWord fieldTo
						oneOfPattern(
							aWord.term(),
							bWord.term()).meta.term),
				term(
					twoWord fieldTo
						oneOfPattern(
							cWord.term(),
							dWord.term()).meta.term)).meta.term)

	@Test
	fun dependentOneOfs_invokeFirstFirst() {
		dependentPatternsFunction!!
			.assertInvokesBody(term(oneWord fieldTo aWord.term))
	}

	@Test
	fun dependentOneOfs_invokeFirstSecond() {
		dependentPatternsFunction!!
			.assertInvokesBody(term(oneWord fieldTo bWord.term))
	}

	@Test
	fun dependentOneOfs_invokeSecondFirst() {
		dependentPatternsFunction!!
			.assertInvokesBody(term(twoWord fieldTo cWord.term))
	}

	@Test
	fun dependentOneOfs_invokeSecondSecond() {
		dependentPatternsFunction!!
			.assertInvokesBody(term(twoWord fieldTo dWord.term))
	}

	private val recursiveFunction = emptyFunction
		.testDefine(
			metaTerm(
				oneOfPattern(
					zeroWord.term(),
					term(
						incrementWord fieldTo
							metaTerm(pattern(recurse(back, back)))))))

//	@Test
//	fun recursive_oneStep() {
//		recursiveFunction!!
//			.assertInvokesBody(zeroWord.term)
//	}
}
