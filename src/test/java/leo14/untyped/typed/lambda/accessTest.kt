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
		compiled(
			"point" lineTo compiled(
				"x" lineTo 10.compiled,
				"y" lineTo 20.compiled))
			.run {
				get("x")!!.eval.assertEqualTo(compiled("x" lineTo 10.compiled))
				get("y")!!.eval.assertEqualTo(compiled("y" lineTo 20.compiled))
				get("z").assertNull
			}
	}

	@Test
	fun getPrimitive() {
		compiled(
			"this" lineTo compiled(
				10.compiledLine,
				"foo".compiledLine,
				Point(10, 20).nativeCompiledLine))
			.run {
				get(numberName)!!.eval.assertEqualTo(10.compiled)
				get(textName)!!.eval.assertEqualTo("foo".compiled)
				get(nativeName)!!.eval.assertEqualTo(Point(10, 20).nativeCompiled)
			}
	}
}