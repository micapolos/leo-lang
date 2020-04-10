package leo14.untyped.typed.lambda

import leo.base.assertEqualTo
import leo14.lambda2.*
import leo14.untyped.className
import leo14.untyped.fieldName
import leo14.untyped.nameName
import leo14.untyped.nativeName
import leo14.untyped.typed.lineTo
import leo14.untyped.typed.nativeType
import leo14.untyped.typed.textType
import leo14.untyped.typed.type
import org.junit.Test

class NativeTest {
	@Test
	fun nativeClassName() {
		type(nativeName lineTo type(className lineTo type("int")))
			.compiled(nil)
			.applyNativeClassName!!
			.eval
			.assertEqualTo(type(className lineTo nativeType).compiled(value(Integer.TYPE)))
	}

	@Test
	fun nativeClassNameText() {
		type(nativeName lineTo type(className lineTo type(nameName lineTo textType)))
			.compiled(value("java.lang.String").functionize)
			.applyNativeClassNameText!!
			.eval
			.assertEqualTo(type(className lineTo nativeType).compiled(value(java.lang.String::class.java)))
	}

	@Test
	fun nativeClassField() {
		type(
			className lineTo nativeType,
			fieldName lineTo type(nameName lineTo textType))
			.compiled(pair(value(java.lang.Integer::class.java))(value("MAX_VALUE")))
			.applyNativeClassField!!
			.eval
			.assertEqualTo(
				type(fieldName lineTo nativeType)
					.compiled(value(java.lang.Integer::class.java.getField("MAX_VALUE"))))
	}
}