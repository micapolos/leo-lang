package leo14.untyped.typed

import leo.base.assertEqualTo
import leo14.invoke
import leo14.leo
import kotlin.test.Test

class IsStaticTest {
	@Test
	fun nonStaticPrimitives() {
		leo("text"()).typeIsStatic.assertEqualTo(false)
		leo("number"()).typeIsStatic.assertEqualTo(false)
		leo("native"()).typeIsStatic.assertEqualTo(false)
		leo("either"()).typeIsStatic.assertEqualTo(false)

		leo("number"(0)).typeIsStatic.assertEqualTo(true)
		leo("text"("foo")).typeIsStatic.assertEqualTo(true)
		leo("native"("Point")).typeIsStatic.assertEqualTo(true)
	}

	@Test
	fun staticModifier() {
		leo("static"("text"())).typeIsStatic.assertEqualTo(true)
		leo("static"("number"())).typeIsStatic.assertEqualTo(true)
		leo("static"("native"())).typeIsStatic.assertEqualTo(true)
		leo("static"("either"())).typeIsStatic.assertEqualTo(true)

		leo("static"("text"("text"()))).typeIsStatic.assertEqualTo(false)
		leo("static"("number"("number"()))).typeIsStatic.assertEqualTo(false)
		leo("static"("native"("native"()))).typeIsStatic.assertEqualTo(false)
		leo("static"("either"("either"()))).typeIsStatic.assertEqualTo(false)
	}

	@Test
	fun struct() {
		leo("point"("x"("number"), "y"("number"()))).typeIsStatic.assertEqualTo(false)
		leo("point"("x"(10), "y"(20))).typeIsStatic.assertEqualTo(true)
	}
}