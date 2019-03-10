package leo.binary

import leo.base.all
import leo.base.assertEqualTo
import kotlin.test.Test

class ParsersTest {
	@Test
	fun bit() {
		bitParser.parse(zero.bit).assertEqualTo(zero.bit.parser)
		bitParser.parse(one.bit).assertEqualTo(one.bit.parser)
		bitParser.parse(zero.bit)?.parse(zero.bit).assertEqualTo(null)
	}

	@Test
	fun int() {
		int5Parser.parse(zero.bit)?.parse(one.bit)?.parse(zero.bit)?.parse(zero.bit)?.parse(one.bit)
			.assertEqualTo(int0.hsb(zero.bit).hsb(one.bit).hsb(zero.bit).hsb(zero.bit).hsb(one.bit).parser)
	}

	@Test
	fun stack() {
		stackParser(3).parse(all(one.bit, one.bit, zero.bit))
			.assertEqualTo(emptyStack.push(one.bit).push(one.bit).push(zero.bit).parser)
	}
}
