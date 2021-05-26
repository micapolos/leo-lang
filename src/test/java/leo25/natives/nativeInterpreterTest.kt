package leo25.natives

import leo.base.assertEqualTo
import leo.base.assertNotNull
import leo14.*
import leo20.patternLineOrNull
import leo25.*
import kotlin.test.Test

class NativeInterpreterTest {
	@Test
	fun textObjectJava() {
		script(
			line(literal("Hello, world!")),
			objectName lineTo script(),
			javaName lineTo script()
		)
			.interpret
			.assertEqualTo(script(javaName lineTo script(objectName lineTo native("Hello, world!"))))
	}

	@Test
	fun javaObjectText() {
		script(
			line(literal("Hello, world!")),
			objectName lineTo script(),
			javaName lineTo script(),
			textName lineTo script()
		)
			.interpret
			.assertEqualTo(script(literal("Hello, world!")))
	}

	@Test
	fun numberObjectJava() {
		script(
			line(literal(123)),
			integerName lineTo script(),
			objectName lineTo script(),
			javaName lineTo script()
		)
			.interpret
			.assertEqualTo(script(javaName lineTo script(objectName lineTo native(123))))
	}

	@Test
	fun javaObjectNumber() {
		script(
			line(literal(123)),
			integerName lineTo script(),
			objectName lineTo script(),
			javaName lineTo script(),
			numberName lineTo script()
		)
			.interpret
			.assertEqualTo(script(literal(123)))
	}

	@Test
	fun arrayObjectJava() {
		script(
			elementName lineTo script(line(literal("foo")), line(objectName), line(javaName)),
			elementName lineTo script(line(literal("bar")), line(objectName), line(javaName)),
			arrayName lineTo script(),
			objectName lineTo script(),
			javaName lineTo script()
		)
			.interpret
			.get(objectName)
			?.get(nativeName)
			.assertNotNull
	}

	@Test
	fun textClassJava() {
		script(
			line(literal("java.lang.String")),
			className lineTo script(),
			javaName lineTo script()
		)
			.interpret
			.assertEqualTo(script(javaName lineTo script(className lineTo native(String::class.java))))
	}

	@Test
	fun javaClassMethod() {
		script(
			line(literal("java.lang.String")),
			className lineTo script(),
			javaName lineTo script(),
			methodName lineTo script(literal("length"))
		)
			.interpret
			.assertEqualTo(
				script(
					javaName lineTo script(
						methodName lineTo native(String::class.java.getMethod("length"))
					)
				)
			)
	}

	@Test
	fun javaObjectInvokeMethod() {
		script(
			line(literal("Hello, world!")),
			objectName lineTo script(),
			javaName lineTo script(),
			invokeName lineTo script(
				line(literal("java.lang.String")),
				className lineTo script(),
				javaName lineTo script(),
				methodName lineTo script(literal("length")),
				argsName lineTo script(line(arrayName), line(objectName), line(javaName))
			)
		)
			.interpret
			.assertEqualTo(
				script(
					javaName lineTo script(
						objectName lineTo native(String::class.java.getMethod("length").invoke("Hello, world!"))
					)
				)
			)
	}

	@Test
	fun textAndText() {
		script(
			line(literal("Hello, ")),
			appendName lineTo script(line(literal("world!")))
		)
			.interpret
			.assertEqualTo(script(literal("Hello, world!")))
	}

	@Test
	fun numberAddNumber() {
		script(
			line(literal(2)),
			plusName lineTo script(line(literal(3)))
		)
			.interpret
			.assertEqualTo(script(literal(5)))
	}

	@Test
	fun numberSubtractNumber() {
		script(
			line(literal(5)),
			minusName lineTo script(line(literal(3)))
		)
			.interpret
			.assertEqualTo(script(literal(2)))
	}

	@Test
	fun numberMultiplyByNumber() {
		script(
			line(literal(2)),
			timesName lineTo script(line(literal(3)))
		)
			.interpret
			.assertEqualTo(script(literal(6)))
	}
}