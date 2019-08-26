package leo13.script

import leo.base.assertEqualTo
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import kotlin.test.Test

class ParserTest {
	@Test
	fun nodeParser_push() {
		val head = script("one" lineTo script()).head
		val parser = head.parser

		parser
			.push(token(opening("foo")))
			.assertEqualTo(head.plus(token(opening("foo")))!!.parser)

		parser
			.push(token(closing))
			.assertEqualTo(head.parser.put(leo13.script.error(token(closing))))
	}

	@Test
	fun errorParser_push() {
		val head = script("one" lineTo script()).head
		val parser = head.parser.put(leo13.script.error(token(closing)))

		parser
			.push(token(opening("foo")))
			.assertEqualTo(parser)

		parser
			.push(token(closing))
			.assertEqualTo(parser)
	}
}