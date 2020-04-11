package leo14.untyped.typed.lambda.core.java

import leo.base.assertEqualTo
import leo14.untyped.className
import leo14.untyped.fieldName
import leo14.untyped.nameName
import leo14.untyped.nativeName
import leo14.untyped.typed.lambda.compiled
import leo14.untyped.typed.lambda.eval
import leo14.untyped.typed.lambda.lineTo
import leo14.untyped.typed.lambda.nativeCompiled
import java.lang.String
import kotlin.test.Test

class RuntimeTest {
	@Test
	fun className() {
		runtimeJavaCore
			.apply(compiled(nativeName lineTo compiled(className lineTo compiled("int"))))!!
			.eval
			.assertEqualTo(compiled(className lineTo Integer.TYPE.nativeCompiled))
	}

	@Test
	fun textClass() {
		runtimeJavaCore
			.apply(
				compiled(
					nativeName lineTo compiled(
						className lineTo compiled(
							nameName lineTo "java.lang.String".compiled))))!!
			.eval
			.assertEqualTo(compiled(className lineTo String::class.java.nativeCompiled))
	}

	@Test
	fun classField() {
		runtimeJavaCore
			.apply(
				compiled(
					className lineTo Integer::class.java.nativeCompiled,
					fieldName lineTo compiled(
						nameName lineTo "MAX_VALUE".compiled)))!!
			.eval
			.assertEqualTo(
				compiled(fieldName lineTo Integer::class.java.getField("MAX_VALUE").nativeCompiled))
	}
}