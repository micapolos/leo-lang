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
		booleanScriptParser.parse(0.bit).assertEqualTo(false.script.parser)
		booleanScriptParser.parse(1.bit).assertEqualTo(true.script.parser)
	}

	@Test
	fun letter() {
		letterOrNullParser.parse(0.bit)?.parse(0.bit)?.parse(0.bit)?.parse(0.bit)?.parse(0.bit)
			.assertEqualTo(Letter.A.parser)
		letterOrNullParser.parse(1.bit)?.parse(1.bit)?.parse(0.bit)?.parse(0.bit)?.parse(1.bit)
			.assertEqualTo(Letter.Z.parser)
		letterOrNullParser.parse(1.bit)?.parse(1.bit)?.parse(0.bit)?.parse(1.bit)?.parse(0.bit)
			.assertEqualTo(null.parser)
		letterOrNullParser.parse(0.bit)?.parse(0.bit)?.parse(0.bit)?.parse(0.bit)?.parse(0.bit)?.parse(0.bit)
			.assertEqualTo(null)
	}

	@Test
	fun character() {
		characterOrNullParser.parse(0.bit)?.parse(0.bit)?.parse(0.bit)?.parse(0.bit)?.parse(0.bit)
			.assertEqualTo(Letter.A.character.parser)
		characterOrNullParser.parse(1.bit)?.parse(1.bit)?.parse(0.bit)?.parse(0.bit)?.parse(1.bit)
			.assertEqualTo(Letter.Z.character.parser)
		characterOrNullParser.parse(1.bit)?.parse(1.bit)?.parse(0.bit)?.parse(1.bit)?.parse(0.bit)
			.assertEqualTo(begin.character.parser)
		characterOrNullParser.parse(1.bit)?.parse(1.bit)?.parse(0.bit)?.parse(1.bit)?.parse(1.bit)
			.assertEqualTo(end.character.parser)
		characterOrNullParser.parse(1.bit)?.parse(1.bit)?.parse(1.bit)?.parse(0.bit)?.parse(0.bit)
			.assertEqualTo(null.parser)
		characterOrNullParser.parse(1.bit)?.parse(1.bit)?.parse(1.bit)?.parse(0.bit)?.parse(0.bit)?.parse(0.bit)
			.assertEqualTo(null)
	}
}