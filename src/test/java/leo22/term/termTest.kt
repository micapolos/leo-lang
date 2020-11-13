package leo22.term

import leo.base.assertEqualTo
import leo14.lambda.arg
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lambda.nativeTerm
import leo21.prim.prim
import leo22.dsl.*
import kotlin.test.Test

class TermTest {
	@Test
	fun termNative() {
		term(native(text("Hello, world!")))
			.termNative_
			.assertEqualTo(nativeTerm(prim("Hello, world!")))

		term(abstraction(term(native(text("Hello, world!")))))
			.termNative_
			.assertEqualTo(fn(nativeTerm(prim("Hello, world!"))))

		term(application(lhs(term(native(number(10)))), rhs(term(native(number(20))))))
			.termNative_
			.assertEqualTo(nativeTerm(prim(10)).invoke(nativeTerm(prim(20))))

		term(variable(number(0)))
			.termNative_
			.assertEqualTo(arg(0))
	}
}