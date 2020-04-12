package leo14.untyped.typed

import leo.base.assertEqualTo
import leo14.invoke
import leo14.leo
import kotlin.test.Test

class ScriptTypeTest {
	@Test
	fun primitives() {
		leo("native"()).type.assertEqualTo(javaType)
		leo("repeating"("native"())).type.assertEqualTo(javaType.repeating.toType)
		leo("recursive"("native"())).type.assertEqualTo(javaType.recursive.toType)
		leo("recurse"()).type.assertEqualTo(recurseType)
	}

	@Test
	fun structs() {
		leo("point"("x"("native"()), "y"("native"())))
			.type
			.assertEqualTo(type("point" lineTo type("x" lineTo javaType, "y" lineTo javaType)))
	}
}