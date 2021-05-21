package leo25.parser

import leo.base.assertEqualTo
import leo.base.tree
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

	@Test
	fun indentedTree() {
		nameParser.indentedTreeParser.run {
			parsed("").assertEqualTo(null)
			parsed("foo").assertEqualTo(null)
			parsed("foo\n").assertEqualTo("foo" treeTo forest())
			parsed("foo\n  bar\n").assertEqualTo("foo" treeTo forest("bar" treeTo forest()))
			parsed("foo\n  bar\n  zoo\n").assertEqualTo("foo" treeTo forest("bar" treeTo forest(), "zoo" treeTo forest()))
		}
	}

	@Test
	fun indentedForest() {
		nameParser.indentedForestParser.run {
			parsed("").assertEqualTo(forest())
			parsed("foo").assertEqualTo(null)
			parsed("foo\n").assertEqualTo(forest("foo" treeTo forest()))
			parsed("foo\nbar\n").assertEqualTo(forest("foo" treeTo forest(), "bar" treeTo forest()))
			parsed("foo\n  bar\n").assertEqualTo(forest("foo" treeTo forest("bar" treeTo forest())))
		}
	}
}