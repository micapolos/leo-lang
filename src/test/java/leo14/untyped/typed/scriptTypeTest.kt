package leo14.untyped.typed

import leo.base.assertEqualTo
import leo14.invoke
import leo14.leo
import kotlin.test.Test

class ScriptTypeTest {
	@Test
	fun primitives() {
		leo("native"()).type.assertEqualTo(nativeType)
		leo("repeating"("native"())).type.assertEqualTo(nativeType.repeating.toType)
		leo("recursive"("native"())).type.assertEqualTo(nativeType.recursive.toType)
		leo("recurse"()).type.assertEqualTo(recurseType)
	}

	@Test
	fun structs() {
		leo("point"("x"("native"()), "y"("native"())))
			.type
			.assertEqualTo(type("point" lineTo type("x" lineTo nativeType, "y" lineTo nativeType)))
	}
}