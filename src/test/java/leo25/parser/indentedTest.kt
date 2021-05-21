package leo25.parser

import leo.base.assertEqualTo
import kotlin.test.Test

class IndentedTest {
	@Test
	fun indented() {
		stringParser.indented(2).run {
			parsed("").assertEqualTo(Indented("", 2))
			parsed(" ").assertEqualTo(null)
			parsed("  ").assertEqualTo(Indented("", 1))
			parsed("   ").assertEqualTo(null)
			parsed("    ").assertEqualTo(Indented("", 0))
			parsed("    a").assertEqualTo(Indented("a", 0))
			parsed("    ab").assertEqualTo(Indented("ab", 0))
			parsed("    ab\n").assertEqualTo(Indented("ab\n", 2))
			parsed("    ab\n ").assertEqualTo(null)
			parsed("    ab\n  ").assertEqualTo(Indented("ab\n", 1))
			parsed("    ab\n   ").assertEqualTo(null)
			parsed("    ab\n    ").assertEqualTo(Indented("ab\n", 0))
			parsed("    ab\n     ").assertEqualTo(Indented("ab\n ", 0))
			parsed("    ab\n     \n").assertEqualTo(Indented("ab\n \n", 2))
		}
	}
}