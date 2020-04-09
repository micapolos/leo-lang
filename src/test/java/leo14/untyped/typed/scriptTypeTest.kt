package leo14.untyped.typed

import leo.base.assertEqualTo
import leo14.invoke
import leo14.leo
import kotlin.test.Test

class ScriptTypeTest {
	@Test
	fun primitives() {
		leo("number"()).type.assertEqualTo(numberType)
		leo("text"()).type.assertEqualTo(textType)
		leo("native"()).type.assertEqualTo(nativeType)
		leo("repeating"("number"())).type.assertEqualTo(numberType.repeating.toType)
		leo("recursive"("number"())).type.assertEqualTo(numberType.recursive.toType)
		leo("recurse"()).type.assertEqualTo(recurseType)
	}

	@Test
	fun structs() {
		leo("point"("x"("number"()), "y"("number"())))
			.type
			.assertEqualTo(type("point" lineTo type("x" lineTo numberType, "y" lineTo numberType)))
	}
}