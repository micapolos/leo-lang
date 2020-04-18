package leo15.type.core

import leo.base.assertEqualTo
import leo14.untyped.typed.valueJavaScriptLine
import leo15.core.anyJava
import java.awt.Point
import kotlin.test.Test

class JavaTest {
	@Test
	fun scriptJava() {
		Point(10, 20).anyJava.scriptLine.assertEqualTo(Point(10, 20).valueJavaScriptLine)
		Point(10, 20).anyJava.value.assertEqualTo(Point(10, 20))
	}
}