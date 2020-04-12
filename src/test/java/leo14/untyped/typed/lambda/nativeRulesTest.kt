package leo14.untyped.typed.lambda

import leo.base.assertEqualTo
import leo14.lambda2.value
import leo14.untyped.className
import leo14.untyped.fieldName
import leo14.untyped.nameName
import leo14.untyped.nativeName
import leo14.untyped.typed.lineTo
import leo14.untyped.typed.nativeType
import leo14.untyped.typed.type
import org.junit.Test

class NativeTest {
	@Test
	fun nativeClassName() {
		typed(nativeName lineTo typed(className lineTo typed("int")))
			.applyNativeClassName!!
			.eval
			.assertEqualTo(type(className lineTo nativeType).typed(value(Integer.TYPE)))
	}

	@Test
	fun nativeClassNameText() {
		typed(nativeName lineTo typed(
			className lineTo typed(
				nameName lineTo "java.lang.String".typed)))
			.applyNativeClassNameText!!
			.eval
			.assertEqualTo(typed(className lineTo java.lang.String::class.java.nativeTyped))
	}

	@Test
	fun nativeClassField() {
		typed(
			className lineTo java.lang.Integer::class.java.nativeTyped,
			fieldName lineTo typed(nameName lineTo "MAX_VALUE".typed))
			.applyNativeClassField!!
			.eval
			.assertEqualTo(
				typed(fieldName lineTo java.lang.Integer::class.java.getField("MAX_VALUE").nativeTyped))
	}
}