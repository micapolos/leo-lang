package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class ExpressionOperationTest {
	@Test
	fun left() {
		leftExpressionOperation
			.atom(atomBindings(), atom(atom(sentence(zeroWord)) linkTo atom(sentence(oneWord))))
			.assertEqualTo(atom(sentence(zeroWord)))
	}

	@Test
	fun right() {
		rightExpressionOperation
			.atom(atomBindings(), atom(atom(sentence(zeroWord)) linkTo atom(sentence(oneWord))))
			.assertEqualTo(atom(sentence(oneWord)))
	}
//
//	@Test
//	fun switch() {
//		operation(switch())
//			.atom(atomBindings(), atom(atom(sentence(zeroWord)) linkTo atom(sentence(oneWord))))
//			.assertEqualTo(atom(sentence(oneWord)))
//	}
}