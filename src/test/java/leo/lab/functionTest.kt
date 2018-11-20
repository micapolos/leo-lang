package leo.lab

import leo.*
import leo.base.assertEqualTo
import kotlin.test.Test

class FunctionTest {
	val testBody = body(term(theWord fieldTo selector().metaTerm), emptyFunction)

	fun Function.testDefine(term: Term<Pattern>): Function? =
		define(term, testBody)

	fun Function.assertInvokesBody(argument: Term<Nothing>) =
		invoke(argument).assertEqualTo(term(theWord fieldTo argument))

	fun Function.assertDoesNotInvokeBody(argument: Term<Nothing>) =
		invoke(argument).assertEqualTo(argument)

	@Test
	fun defineSimpleAndInvoke_match() {
		emptyFunction
			.testDefine(aWord.term())!!
			.assertInvokesBody(aWord.term)
	}

	@Test
	fun defineSimpleAndInvoke_mismatch() {
		emptyFunction
			.testDefine(aWord.term())!!
			.assertDoesNotInvokeBody(bWord.term)
	}

	@Test
	fun defineTwo_getFirst() {
		emptyFunction
			.testDefine(ageWord.term())!!
			.testDefine(nameWord.term())!!
			.assertInvokesBody(ageWord.term)
	}

	@Test
	fun defineTwo_getSecond() {
		emptyFunction
			.testDefine(ageWord.term())!!
			.testDefine(nameWord.term())!!
			.assertInvokesBody(nameWord.term)
	}

	@Test
	fun defineCommonPrefix_getFirst() {
		emptyFunction
			.testDefine(bodyWord.term())!!
			.testDefine(booleanWord.term())!!
			.assertInvokesBody(bodyWord.term)
	}

	@Test
	fun defineCommonPrefix_getSecond() {
		emptyFunction
			.testDefine(bodyWord.term())!!
			.testDefine(booleanWord.term())!!
			.assertInvokesBody(booleanWord.term)
	}

	@Test
	fun redefineSame() {
		emptyFunction
			.testDefine(aWord.term())!!
			.testDefine(aWord.term())
			.assertEqualTo(null)
	}

	@Test
	fun redefineLonger() {
		emptyFunction
			.testDefine(term(aWord.field))!!
			.testDefine(term(aWord.field, bWord.field))
			.assertEqualTo(null)
	}

	@Test
	fun redefineShorter() {
		emptyFunction
			.testDefine(term(aWord.field, bWord.field))!!
			.testDefine(term(aWord.field))
			.assertEqualTo(null)
	}

	@Test
	fun defineWithoutAndWithRhs_invokeWithoutRhs() {
		emptyFunction
			.testDefine(term(aWord.field))!!
			.testDefine(term(aWord fieldTo bWord.term))!!
			.assertInvokesBody(aWord.term)
	}

	@Test
	fun defineWithoutAndWithRhs_invokeWithRhs() {
		emptyFunction
			.testDefine(term(aWord.field))!!
			.testDefine(term(aWord fieldTo bWord.term))!!
			.assertInvokesBody(term(aWord fieldTo bWord.term))
	}

	@Test
	fun defineOneOf_invokeFirst() {
		emptyFunction
			.testDefine(pattern(aWord.term(), bWord.term()).metaTerm)!!
			.assertInvokesBody(aWord.term)
	}

	@Test
	fun defineOneOf_invokeSecond() {
		emptyFunction
			.testDefine(pattern(aWord.term(), bWord.term()).metaTerm)!!
			.assertInvokesBody(bWord.term)
	}

	val nonDependentOneOfsFunction = emptyFunction
		.testDefine(
			term(
				oneWord fieldTo pattern(aWord.term(), bWord.term()).metaTerm,
				twoWord fieldTo pattern(aWord.term(), bWord.term()).metaTerm))

	@Test
	fun defineNonDependentOneOfs_getFirstFirst() {
		nonDependentOneOfsFunction!!
			.assertInvokesBody(term(oneWord fieldTo aWord.term, twoWord fieldTo aWord.term))
	}

	@Test
	fun defineNonDependentOneOfs_getFirstSecond() {
		nonDependentOneOfsFunction!!
			.assertInvokesBody(term(oneWord fieldTo aWord.term, twoWord fieldTo bWord.term))
	}

	@Test
	fun defineNonDependentOneOfs_getSecondFirst() {
		nonDependentOneOfsFunction!!
			.assertInvokesBody(term(oneWord fieldTo bWord.term, twoWord fieldTo aWord.term))
	}

	@Test
	fun defineNonDependentOneOfs_getSecondSecond() {
		nonDependentOneOfsFunction!!
			.assertInvokesBody(term(oneWord fieldTo bWord.term, twoWord fieldTo bWord.term))
	}

	val dependentOneOfsFunction = emptyFunction
		.testDefine(
			metaTerm(
				pattern(
					term(
						oneWord fieldTo metaTerm(
							pattern(
								aWord.term(),
								bWord.term()))),
					term(
						twoWord fieldTo metaTerm(
							pattern(
								cWord.term(),
								dWord.term()))))))

	@Test
	fun dependentOneOfs_invokeFirstFirst() {
		dependentOneOfsFunction!!
			.assertInvokesBody(term(oneWord fieldTo aWord.term))
	}

	@Test
	fun dependentOneOfs_invokeFirstSecond() {
		dependentOneOfsFunction!!
			.assertInvokesBody(term(oneWord fieldTo bWord.term))
	}

	@Test
	fun dependentOneOfs_invokeSecondFirst() {
		dependentOneOfsFunction!!
			.assertInvokesBody(term(twoWord fieldTo cWord.term))
	}

	@Test
	fun dependentOneOfs_invokeSecondSecond() {
		dependentOneOfsFunction!!
			.assertInvokesBody(term(twoWord fieldTo dWord.term))
	}
}
