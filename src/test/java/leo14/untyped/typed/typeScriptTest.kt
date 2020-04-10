package leo14.untyped.typed

import leo.base.assertEqualTo
import leo14.invoke
import leo14.leo
import leo14.literal
import leo14.untyped.*
import kotlin.test.Test

class TypeScriptTest {
	@Test
	fun static() {
		emptyType.script.assertEqualTo(leo())
		emptyType.plus(literal("foo").typeLine).script.assertEqualTo(leo("foo"))
		emptyType.plus(literal(123).typeLine).script.assertEqualTo(leo(123))
	}

	@Test
	fun exact() {
		emptyType.plus(textName lineTo emptyType).script.assertEqualTo(leo(exactName(textName())))
		emptyType.plus(numberName lineTo emptyType).script.assertEqualTo(leo(exactName(numberName())))
		emptyType.plus(nativeName lineTo emptyType).script.assertEqualTo(leo(exactName(nativeName())))
		emptyType.plus(orName lineTo emptyType).script.assertEqualTo(leo(exactName(orName())))
		emptyType.plus(repeatingName lineTo emptyType).script.assertEqualTo(leo(exactName(repeatingName())))
		emptyType.plus(recursiveName lineTo emptyType).script.assertEqualTo(leo(exactName(recursiveName())))
		emptyType.plus(recurseName lineTo emptyType).script.assertEqualTo(leo(exactName(recurseName())))
		emptyType.plus(exactName lineTo emptyType).script.assertEqualTo(leo(exactName(exactName())))
		emptyType.plus(anythingName lineTo emptyType).script.assertEqualTo(leo(exactName(anythingName())))
		emptyType.plus(nothingName lineTo emptyType).script.assertEqualTo(leo(exactName(nothingName())))
	}

	@Test
	fun primitives() {
		emptyType.plus(nativeTypeLine).script.assertEqualTo(leo(nativeName()))
	}

	@Test
	fun alternatives() {
		emptyType
			.plus("true" lineTo emptyType)
			.or(emptyType.plus("false" lineTo emptyType))
			.script
			.assertEqualTo(leo("true"(), orName("false"())))
	}

	@Test
	fun recursive() {
		emptyType.recursive.toType.script.assertEqualTo(leo(recursiveName()))
		emptyType.plus(nativeTypeLine).recursive.toType.script.assertEqualTo(leo(recursiveName(nativeName())))
	}

	@Test
	fun recurse() {
		recurseType.script.assertEqualTo(leo(recurseName()))
	}

	@Test
	fun function() {
		emptyType.plus(nativeTypeLine)
			.functionTo(emptyType.plus(nativeTypeLine))
			.type
			.script
			.assertEqualTo(leo(functionName(nativeName(), doingName(nativeName()))))
	}

	@Test
	fun literals() {
		emptyType.plus(literal(123).typeLine).script.assertEqualTo(leo(123))
		emptyType.plus(literal("foo").typeLine).script.assertEqualTo(leo("foo"))
	}

	@Test
	fun repeating() {
		nativeType.repeating.toType.script.assertEqualTo(leo(repeatingName(nativeName())))
	}
}