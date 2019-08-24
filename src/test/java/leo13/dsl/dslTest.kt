package leo13.dsl

import kotlin.test.Test

class DslTest {
	@Test
	fun testSyntax() {
		switch(
			case(zero(), _to(expr())),
			case(one(), _to(expr())))
	}
}