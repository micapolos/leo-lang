package leo15.type

import leo.base.assertEqualTo
import leo14.invoke
import leo14.leo
import leo14.literal
import kotlin.test.Test

class TypeTest {
	@Test
	fun scriptType() {
		leo().type.assertEqualTo(emptyType)
		leo(10).type.assertEqualTo(literal(10).type)
		leo("foo").type.assertEqualTo(literal("foo").type)
		leo("foo"()).type.assertEqualTo(type("foo"))
	}

	@Test
	fun choice() {
		leo("choice"("true"(), "false"()))
			.type
			.assertEqualTo(type(choice("true" lineTo emptyType, "false" lineTo emptyType)))
	}

	@Test
	fun builtin() {
		leo("number"()).type.assertEqualTo(numberType)
		leo("text"()).type.assertEqualTo(textType)
		leo("java"()).type.assertEqualTo(javaType)
	}

	@Test
	fun repeating() {
		leo("repeating"("number"())).type.assertEqualTo(numberTypeLine.choice.repeating.toType)
	}

	@Test
	fun recursive() {
		leo("recursive"("number"())).type.assertEqualTo(numberType.recursive.toType)
		leo("recurse"()).type.assertEqualTo(recurseType)
	}
}