package leo32.runtime

import leo.base.assertEqualTo
import kotlin.test.Test

class LeoTest {
	@Test
	fun partials() {
		"".assertGives("")
		"a".assertGives("a")
		"a(".assertGives("a(")
		"a()".assertGives("a()")
		"a()b".assertGives("a()b")
		"a()b(".assertGives("a()b(")
		"a()b()".assertGives("b(a())")
		"a(b".assertGives("a(b")
		"a(b(".assertGives("a(b(")
		"a(b()".assertGives("a(b()")
		"a(b())".assertGives("a(b())")

		"(".assertGives("(<<<ERROR")
		")".assertGives(")<<<ERROR")
		"a)".assertGives("a)<<<ERROR")
		"a((".assertGives("a((<<<ERROR")
	}

	@Test
	fun leo() {
		"a()equals(a())".assertGives("boolean(true())")
		"quote(a()equals(a()))".assertGives("a()equals(a())")
		"x(zero())x()".assertGives("zero()")
	}
}

fun String.assertGives(string: String) =
	leo.assertEqualTo(string)