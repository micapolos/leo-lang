package leo14.untyped.typed

import leo.base.assertEqualTo
import leo14.invoke
import leo14.leo
import leo14.number
import java.awt.Point
import kotlin.test.Test

class ValueTest {
	@Test
	fun valueScript() {
		"foo".valueScript.assertEqualTo(leo("foo"))
		number(1).valueScript.assertEqualTo(leo(1))
		true.valueScript.assertEqualTo(leo("boolean"("native"("true"))))
		1.valueScript.assertEqualTo(leo("int"("native"("1"))))
	}

	@Test
	fun nativeScript() {
		Point(20, 40).nativeScript.assertEqualTo(leo("native"(Point(20, 40).toString())))
	}
}