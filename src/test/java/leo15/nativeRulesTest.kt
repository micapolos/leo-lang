package leo15

import leo.base.assertEqualTo
import leo14.untyped.className
import leo14.untyped.fieldName
import leo14.untyped.javaName
import leo14.untyped.nameName
import org.junit.Test
import java.lang.String

class NativeTest {
	@Test
	fun javaClassName() {
		typed(javaName lineTo typed(className lineTo typed("int")))
			.applyJavaClassName!!
			.eval
			.assertEqualTo(typed(className lineTo Integer.TYPE.valueJavaTyped))
	}

	@Test
	fun javaClassNameText() {
		typed(javaName lineTo typed(
			className lineTo typed(
				nameName lineTo "java.lang.String".typed)))
			.applyJavaClassNameText!!
			.eval
			.assertEqualTo(typed(className lineTo String::class.java.valueJavaTyped))
	}

	@Test
	fun javaClassField() {
		typed(
			className lineTo Integer::class.java.valueJavaTyped,
			fieldName lineTo typed(nameName lineTo "MAX_VALUE".typed))
			.applyJavaClassField!!
			.eval
			.assertEqualTo(
				typed(fieldName lineTo Integer::class.java.getField("MAX_VALUE").valueJavaTyped))
	}
}