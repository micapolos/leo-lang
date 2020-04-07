package leo14.untyped.typed

import leo.base.assertEqualTo
import leo14.invoke
import leo14.leo
import leo14.literal
import kotlin.test.Test

class TypeScriptTest {
	@Test
	fun static() {
		emptyType.script.assertEqualTo(leo())
		emptyType.plus(literal("foo").staticTypeLine).script.assertEqualTo(leo("foo"))
		emptyType.plus(literal(123).staticTypeLine).script.assertEqualTo(leo(123))
	}

	@Test
	fun exact() {
		emptyType.plus("text" lineTo emptyType).script.assertEqualTo(leo("exact"("text"())))
		emptyType.plus("number" lineTo emptyType).script.assertEqualTo(leo("exact"("number"())))
		emptyType.plus("native" lineTo emptyType).script.assertEqualTo(leo("exact"("native"())))
		emptyType.plus("or" lineTo emptyType).script.assertEqualTo(leo("exact"("or"())))
		emptyType.plus("recursive" lineTo emptyType).script.assertEqualTo(leo("exact"("recursive"())))
		emptyType.plus("recurse" lineTo emptyType).script.assertEqualTo(leo("exact"("recurse"())))
		emptyType.plus("exact" lineTo emptyType).script.assertEqualTo(leo("exact"("exact"())))
	}

	@Test
	fun primitives() {
		emptyType.plus(textTypeLine).script.assertEqualTo(leo("text"()))
		emptyType.plus(numberTypeLine).script.assertEqualTo(leo("number"()))
		emptyType.plus(nativeTypeLine).script.assertEqualTo(leo("native"()))
	}

	@Test
	fun alternatives() {
		emptyType
			.plus("true" lineTo emptyType)
			.or(emptyType.plus("false" lineTo emptyType))
			.script
			.assertEqualTo(leo("true"(), "or"("false"())))
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