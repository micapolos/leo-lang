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
		compiled(nativeName lineTo compiled(className lineTo compiled("int")))
			.applyNativeClassName!!
			.eval
			.assertEqualTo(type(className lineTo nativeType).compiled(value(Integer.TYPE)))
	}

	@Test
	fun nativeClassNameText() {
		compiled(nativeName lineTo compiled(
			className lineTo compiled(
				nameName lineTo "java.lang.String".compiled)))
			.applyNativeClassNameText!!
			.eval
			.assertEqualTo(compiled(className lineTo java.lang.String::class.java.nativeCompiled))
	}

	@Test
	fun nativeClassField() {
		compiled(
			className lineTo java.lang.Integer::class.java.nativeCompiled,
			fieldName lineTo compiled(nameName lineTo "MAX_VALUE".compiled))
			.applyNativeClassField!!
			.eval
			.assertEqualTo(
				compiled(fieldName lineTo java.lang.Integer::class.java.getField("MAX_VALUE").nativeCompiled))
	}
}