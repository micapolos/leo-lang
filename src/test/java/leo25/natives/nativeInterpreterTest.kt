package leo25.natives

import leo.base.assertEqualTo
import leo14.line
import leo14.lineTo
import leo14.literal
import leo14.script
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
			javaName lineTo script()
		)
			.interpret
			.assertEqualTo(script(javaName lineTo script(integerName lineTo native(123))))
	}

	@Test
	fun javaIntegerNumber() {
		script(
			line(literal(123)),
			integerName lineTo script(),
			javaName lineTo script(),
			numberName lineTo script()
		)
			.interpret
			.assertEqualTo(script(literal(123)))
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
				methodName lineTo script(literal("length"))
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
	fun textLength() {
		script(
			line(literal("Hello, world!")),
			lengthName lineTo script()
		)
			.interpret
			.assertEqualTo(script(lengthName lineTo script(literal(13))))
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