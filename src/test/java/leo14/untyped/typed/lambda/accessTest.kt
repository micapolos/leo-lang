package leo14.untyped.typed.lambda

import leo.base.assertEqualTo
import leo.base.assertNull
import leo14.untyped.nativeName
import leo14.untyped.numberName
import leo14.untyped.textName
import java.awt.Point
import kotlin.test.Test

class AccessTest {
	@Test
	fun getField() {
		typed(
			"point" lineTo typed(
				"x" lineTo 10.typed,
				"y" lineTo 20.typed))
			.run {
				get("x")!!.eval.assertEqualTo(typed("x" lineTo 10.typed))
				get("y")!!.eval.assertEqualTo(typed("y" lineTo 20.typed))
				get("z").assertNull
			}
	}

	@Test
	fun getPrimitive() {
		typed(
			"this" lineTo typed(
				10.typedLine,
				"foo".typedLine,
				Point(10, 20).nativeTypedLine))
			.run {
				get(numberName)!!.eval.assertEqualTo(10.typed)
				get(textName)!!.eval.assertEqualTo("foo".typed)
				get(nativeName)!!.eval.assertEqualTo(Point(10, 20).nativeTyped)
			}
	}
}