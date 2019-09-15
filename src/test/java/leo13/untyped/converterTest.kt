package leo13.untyped

import leo.base.assertEqualTo
import leo13.assertFailsWith
import leo13.converterCapture
import leo13.script.lineTo
import leo13.script.script
import leo13.token.Token
import leo13.traced
import leo13.untyped.expression.Case
import leo13.untyped.expression.caseTo
import leo13.untyped.expression.expression
import kotlin.test.Test

class ConverterTest {
	@Test
	fun capture_ok() {
		converterCapture<Case, Token> {
			convert("one" caseTo expression())
		}.assertEqualTo("one" caseTo expression())
	}

	@Test
	fun capture_notCaptured() {
		traced {
			converterCapture<Case, Token> {}
		}.assertFailsWith("not" lineTo script("captured"))
	}

	@Test
	fun capture_alreadyCaptured() {
		traced {
			converterCapture<Case, Token> {
				convert("one" caseTo expression())
				convert("two" caseTo expression())
			}
		}.assertFailsWith("already" lineTo script("captured"))
	}
}