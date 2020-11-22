package leo21.compiled

import leo.base.assertEqualTo
import leo14.lambda.invoke
import leo14.lambda.nativeTerm
import leo21.prim.StringTryNumberPrim
import leo21.prim.prim
import leo21.type.choice
import leo21.type.line
import leo21.type.lineTo
import leo21.type.numberType
import leo21.type.stringLine
import leo21.type.type
import kotlin.test.Test

class PrimTest {
	@Test
	fun try_() {
		compiled("123")
			.stringTryNumber
			.assertEqualTo(
				nativeTerm(StringTryNumberPrim)
					.invoke(nativeTerm(prim("123")))
					.of(type(
						"try" lineTo type(
							line(
								choice(
									"success" lineTo numberType,
									"error" lineTo type(
										stringLine,
										"try" lineTo type(
											"number" lineTo type()))))))))
	}
}