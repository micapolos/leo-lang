package leo13.script.parser

import leo.base.assertEqualTo
import leo13.*
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
			.assertEqualTo(head.parser.put(error(token(closing))))
	}

	@Test
	fun errorParser_push() {
		val head = script("one" lineTo script()).head
		val parser = head.parser.put(error(token(closing)))

		parser
			.push(token(opening("foo")))
			.assertEqualTo(parser)

		parser
			.push(token(closing))
			.assertEqualTo(parser)
	}
}