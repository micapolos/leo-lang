package leo25.parser

import leo.base.assertEqualTo
import kotlin.test.Test

class TreeTest {
	@Test
	fun parenthesizedTreeParser() {
		nameParser
			.parenthesisedTreeParser
			.parsed("()")
			.assertEqualTo(null)

		nameParser
			.parenthesisedTreeParser
			.parsed("foo()")
			.assertEqualTo("foo" treeTo forest())
	}

	@Test
	fun parenthesizedForestParser() {
		nameParser
			.parenthesisedForestParser
			.parsed("")
			.assertEqualTo(forest())

		nameParser
			.parenthesisedForestParser
			.parsed("foo()")
			.assertEqualTo(forest("foo" treeTo forest()))

		nameParser
			.parenthesisedForestParser
			.parsed("foo()bar()")
			.assertEqualTo(forest("foo" treeTo forest(), "bar" treeTo forest()))

		nameParser
			.parenthesisedForestParser
			.parsed("foo(bar())")
			.assertEqualTo(forest("foo" treeTo forest("bar" treeTo forest())))
	}
}