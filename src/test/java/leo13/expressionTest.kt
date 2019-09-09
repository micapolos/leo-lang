package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class ExpressionTest {
	@Test
	fun atomExpression() {
		expression(atom(sentence(zeroWord)))
			.atom(atomBindings())
			.assertEqualTo(atom(sentence(zeroWord)))
	}

	@Test
	fun bindingExpression() {
		expression(lastExpressionBinding)
			.atom(atomBindings(atom(sentence(zeroWord)), atom(sentence(oneWord))))
			.assertEqualTo(atom(sentence(oneWord)))

		expression(lastExpressionBinding.previous)
			.atom(atomBindings(atom(sentence(zeroWord)), atom(sentence(oneWord))))
			.assertEqualTo(atom(sentence(zeroWord)))
	}

	@Test
	fun linkExpression() {
		expression(atom(atom(sentence(zeroWord)) linkTo atom(sentence(oneWord))))
			.linkTo(leftExpressionOperation)
			.atom(atomBindings())
			.assertEqualTo(atom(sentence(zeroWord)))
	}
}