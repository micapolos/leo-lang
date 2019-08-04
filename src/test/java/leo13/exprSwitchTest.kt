package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class ExprSwitchTest {
	@Test
	fun fromType() {
		("switch" lineTo type(
			"case" lineTo type(
				"one" lineTo type(
					"1" lineTo type())),
			"case" lineTo type(
				"two" lineTo type(
					"1" lineTo type(),
					"2" lineTo type()))))
			.exprSwitchOrNull
			.assertEqualTo(
				switch(
					"one" caseTo expr(0 lineTo expr()),
					"two" caseTo expr(
						0 lineTo expr(),
						0 lineTo expr())))
	}
}