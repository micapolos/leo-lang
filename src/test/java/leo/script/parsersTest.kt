package leo.script

import leo.Letter
import leo.base.assertEqualTo
import leo.begin
import leo.binary.*
import leo.character
import leo.end
import org.junit.Test

class ParsersTest {
	@Test
	fun boolean() {
		booleanScriptParser.parse(zero.bit).assertEqualTo(false.script.parser)
		booleanScriptParser.parse(one.bit).assertEqualTo(true.script.parser)
	}

	@Test
	fun letter() {
		letterOrNullParser.parse(zero.bit)?.parse(zero.bit)?.parse(zero.bit)?.parse(zero.bit)?.parse(zero.bit)
			.assertEqualTo(Letter.A.parser)
		letterOrNullParser.parse(one.bit)?.parse(one.bit)?.parse(zero.bit)?.parse(zero.bit)?.parse(one.bit)
			.assertEqualTo(Letter.Z.parser)
		letterOrNullParser.parse(one.bit)?.parse(one.bit)?.parse(zero.bit)?.parse(one.bit)?.parse(zero.bit)
			.assertEqualTo(null.parser)
		letterOrNullParser.parse(zero.bit)?.parse(zero.bit)?.parse(zero.bit)?.parse(zero.bit)?.parse(zero.bit)?.parse(zero.bit)
			.assertEqualTo(null)
	}

	@Test
	fun character() {
		characterOrNullParser.parse(zero.bit)?.parse(zero.bit)?.parse(zero.bit)?.parse(zero.bit)?.parse(zero.bit)
			.assertEqualTo(Letter.A.character.parser)
		characterOrNullParser.parse(one.bit)?.parse(one.bit)?.parse(zero.bit)?.parse(zero.bit)?.parse(one.bit)
			.assertEqualTo(Letter.Z.character.parser)
		characterOrNullParser.parse(one.bit)?.parse(one.bit)?.parse(zero.bit)?.parse(one.bit)?.parse(zero.bit)
			.assertEqualTo(begin.character.parser)
		characterOrNullParser.parse(one.bit)?.parse(one.bit)?.parse(zero.bit)?.parse(one.bit)?.parse(one.bit)
			.assertEqualTo(end.character.parser)
		characterOrNullParser.parse(one.bit)?.parse(one.bit)?.parse(one.bit)?.parse(zero.bit)?.parse(zero.bit)
			.assertEqualTo(null.parser)
		characterOrNullParser.parse(one.bit)?.parse(one.bit)?.parse(one.bit)?.parse(zero.bit)?.parse(zero.bit)?.parse(zero.bit)
			.assertEqualTo(null)
	}
}