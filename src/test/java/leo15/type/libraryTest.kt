package leo15.type

import leo.base.assertEqualTo
import leo15.givenName
import leo15.givesName
import leo15.isName
import leo15.lambda.at
import kotlin.test.Test

class LibraryTest {
	@Test
	fun applyIs() {
		emptyLibrary
			.plus(type("foo").key bindingTo typed("bar").value)
			.applyIs(
				typed(
					"zoo" lineTo emptyTyped,
					isName lineTo typed("zar")))
			.assertEqualTo(
				emptyLibrary
					.plus(type("foo").key bindingTo typed("bar").value)
					.plus(type("zoo").key bindingTo typed("zar").value))
	}

	@Test
	fun applyGives() {
		emptyLibrary
			.plus(type("foo").key bindingTo typed("bar").value)
			.run {
				applyGives(
					typed(
						"zoo" lineTo emptyTyped,
						givesName lineTo typed(givenName)))
					.assertEqualTo(
						plus(type("zoo").key bindingTo
							scope
								.with(typed(givenName lineTo at(0).dynamicExpression.of(type("zoo"))))
								.value))
			}
	}
}