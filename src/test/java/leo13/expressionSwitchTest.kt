package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class ExpressionSwitchTest {
	@Test
	fun atom() {
		switch(
			operation(plus(expression(atom(sentence(zeroWord))))),
			operation(plus(expression(atom(sentence(oneWord))))))
			.atom(atomBindings(), atom(atom linkTo atom(sentence(pointWord))))
			.assertEqualTo(null)
	}
}
