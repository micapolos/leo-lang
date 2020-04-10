package leo14.untyped.typed.lambda.core.java

import leo.base.assertEqualTo
import leo14.lambda2.invoke
import leo14.lambda2.nil
import leo14.lambda2.pair
import leo14.lambda2.valueTerm
import leo14.untyped.className
import leo14.untyped.fieldName
import leo14.untyped.nameName
import leo14.untyped.nativeName
import leo14.untyped.typed.lambda.compiled
import leo14.untyped.typed.lambda.eval
import leo14.untyped.typed.lineTo
import leo14.untyped.typed.nativeType
import leo14.untyped.typed.textType
import leo14.untyped.typed.type
import java.lang.String
import kotlin.test.Test

class RuntimeTest {
	@Test
	fun className() {
		runtimeJavaCore
			.apply(
				type(nativeName lineTo type(className lineTo type("int")))
					.compiled(nil))!!
			.eval
			.assertEqualTo(type(className lineTo nativeType).compiled(Integer.TYPE.valueTerm))
	}

	@Test
	fun textClass() {
		runtimeJavaCore
			.apply(
				type(nativeName lineTo type(className lineTo type(nameName lineTo textType)))
					.compiled("java.lang.String".valueTerm))!!
			.eval
			.assertEqualTo(type(className lineTo nativeType).compiled(String::class.java.valueTerm))
	}

	@Test
	fun classField() {
		runtimeJavaCore
			.apply(
				type(
					className lineTo nativeType,
					fieldName lineTo type(nameName lineTo textType))
					.compiled(pair(Integer::class.java.valueTerm)("MAX_VALUE".valueTerm)))!!
			.eval
			.assertEqualTo(
				type(fieldName lineTo nativeType)
					.compiled(Integer::class.java.getField("MAX_VALUE").valueTerm))
	}
}