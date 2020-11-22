package leo21.compiled

import leo.base.assertEqualTo
import leo14.lambda.invoke
import leo14.lambda.nativeTerm
import leo21.prim.StringTryNumberPrim
import leo21.prim.prim
import leo21.type.numberType
import leo21.type.try_
import kotlin.test.Test

class PrimTest {
	@Test
	fun try_() {
		compiled("123")
			.stringTryNumber
			.assertEqualTo(
				nativeTerm(StringTryNumberPrim)
					.invoke(nativeTerm(prim("123")))
					.of(numberType.try_))
	}
}