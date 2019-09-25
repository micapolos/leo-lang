package leo13

import leo.base.assertEqualTo
import leo13.compiler.TypedExpression
import leo13.compiler.typed
import leo13.compiler.compiler
import leo13.expression.expression
import leo13.type.type
import leo13.token.Token
import leo13.tokenizer.tokenizer
import kotlin.test.Test

class EndToEndTest {
//	@Test
//	fun interpreting() {
//		converterCapture<Interpreted, Token> {
//			interpreter().tokenizer().charProcess("jajko\n").process(endOfTransmissionChar)
//		}.assertEqualTo(interpreted(value("jajko"), type("jajko")))
//	}

	@Test
	fun compiling() {
		converterCapture<TypedExpression, Token> {
			compiler().tokenizer().charProcess("jajko\n").process(endOfTransmissionChar)
		}.assertEqualTo(
			typed(
				expression("jajko"),
				type("jajko")))
	}
}
