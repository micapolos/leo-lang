package leo25.parser

import leo.base.assertEqualTo
import kotlin.test.Test

class TreeTest {
	@Test
	fun parenthesizedTreeParser() {
		stringParser
			.parenthesisedTreeParser
			.parsed("")
			.assertEqualTo("" treeTo forest())
	}

	@Test
	fun parenthesizedForestParser() {
		stringParser
			.parenthesisedForestParser
			.parsed("")
			.assertEqualTo(forest())

		stringParser
			.parenthesisedForestParser
			.parsed("foo()")
			.assertEqualTo(forest("foo" treeTo forest()))
	}
}