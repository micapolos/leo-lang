package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class ExpressionSwitchTest {
	@Test
	fun atom() {
		val switch = switch(
			operation(plus(expression(atom(sentence(zeroWord))))),
			operation(plus(expression(atom(sentence(oneWord))))))

		switch
			.atom(atomBindings(), atom(atom linkTo atom(sentence(pointWord))))
			.assertEqualTo(atom(atom(sentence(pointWord)) linkTo atom(sentence(oneWord))))

		switch
			.atom(atomBindings(), atom(atom(atom linkTo atom) linkTo atom(sentence(pointWord))))
			.assertEqualTo(atom(atom(sentence(pointWord)) linkTo atom(sentence(zeroWord))))
	}
}
