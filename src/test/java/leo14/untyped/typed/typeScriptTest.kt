package leo14.untyped.typed

import leo.base.assertEqualTo
import leo14.invoke
import leo14.leo
import leo14.literal
import kotlin.test.Test

class TypeScriptTest {
	@Test
	fun static() {
		leo().static.type.script.assertEqualTo(leo())
		leo("text"()).static.type.script.assertEqualTo(leo("static"("text"())))
		leo("number"()).static.type.script.assertEqualTo(leo("static"("number"())))
		leo("native"()).static.type.script.assertEqualTo(leo("static"("native"())))
		leo("foo").static.type.script.assertEqualTo(leo("static"("foo")))
		leo(123).static.type.script.assertEqualTo(leo("static"(123)))
	}

	@Test
	fun primitives() {
		emptyType.plus(textTypeLine).script.assertEqualTo(leo("text"()))
		emptyType.plus(numberTypeLine).script.assertEqualTo(leo("number"()))
		emptyType.plus(nativeTypeLine).script.assertEqualTo(leo("native"()))
	}

	@Test
	fun choices() {
		emptyType
			.plus(emptyChoice.line)
			.script
			.assertEqualTo(leo("either"()))

		emptyType
			.plus(emptyChoice.plus(textTypeLine).line)
			.script
			.assertEqualTo(leo("either"("text"())))

		emptyType
			.plus(emptyChoice.plus(textTypeLine).plus(numberTypeLine).line)
			.script
			.assertEqualTo(leo("either"("text"(), "number"())))
	}

	@Test
	fun recursive() {
		emptyType.recursive.toType.script.assertEqualTo(leo("recursive"()))
		emptyType.plus(textTypeLine).recursive.toType.script.assertEqualTo(leo("recursive"("text"())))
	}

	@Test
	fun recurse() {
		recurseType.script.assertEqualTo(leo("recurse"()))
	}

	@Test
	fun function() {
		emptyType.plus(numberTypeLine)
			.functionTo(emptyType.plus(textTypeLine))
			.type
			.script
			.assertEqualTo(leo("function"("number"(), "doing"("text"()))))
	}

	@Test
	fun literals() {
		emptyType.plus(literal(123).staticTypeLine).script.assertEqualTo(leo(123))
		emptyType.plus(literal("foo").staticTypeLine).script.assertEqualTo(leo("foo"))
	}
}