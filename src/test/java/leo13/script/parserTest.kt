package leo13.script

import leo.base.assertEqualTo
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import kotlin.test.Test

class ScriptParserTest {
	@Test
	fun nodeParser_push() {
		val head = script("one" lineTo script()).head
		val parser = head.scriptParser

		parser
			.push(token(opening("foo")))
			.assertEqualTo(head.plus(token(opening("foo")))!!.scriptParser)

		parser
			.push(token(closing))
			.assertEqualTo(head.scriptParser.put(leo13.script.error(token(closing))))
	}

	@Test
	fun errorParser_push() {
		val head = script("one" lineTo script()).head
		val parser = head.scriptParser.put(leo13.script.error(token(closing)))

		parser
			.push(token(opening("foo")))
			.assertEqualTo(parser)

		parser
			.push(token(closing))
			.assertEqualTo(parser)
	}
}