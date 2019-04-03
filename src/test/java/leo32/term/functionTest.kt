package leo32.term

import leo.base.assertEqualTo
import leo.base.empty
import kotlin.test.Test

class FunctionTest {
	@Test
	fun invoke() {
		val bitZeroTerm = term("bit" fieldTo term("zero"))
		val bitOneTerm = term("bit" fieldTo term("zero"))
		val typeBitTerm = term("type" fieldTo term("bit"))
		val bitType = typeBitTerm.type

		val typeResolver = empty.typeResolver
			.put(bitZeroTerm, bitType)
			.put(bitOneTerm, bitType)

		val nandTermResolver = empty.termResolver
			.put(bitZeroTerm.plus("nand" fieldTo bitZeroTerm), bitOneTerm)
			.put(bitZeroTerm.plus("nand" fieldTo bitOneTerm), bitZeroTerm)
			.put(bitOneTerm.plus("nand" fieldTo bitZeroTerm), bitZeroTerm)
			.put(bitOneTerm.plus("nand" fieldTo bitOneTerm), bitZeroTerm)

		val templateResolver = empty.templateResolver
			.put(typeBitTerm.plus("nand" fieldTo typeBitTerm).type, template(selector(nandTermResolver.getter)))
			.put(term("not" fieldTo typeBitTerm).type, template(selector("not".getter), "nand" fieldTo template(selector("not".getter))))

		val function = Function(typeResolver, templateResolver)

		typeResolver
			.resolve(bitZeroTerm.plus("nand" fieldTo bitZeroTerm))
			.assertEqualTo(typeBitTerm.plus("nand" fieldTo typeBitTerm).type)

		function
			.invoke(bitZeroTerm.plus("nand" fieldTo bitZeroTerm))
			.assertEqualTo(null)

		function
			.invoke(term("not" fieldTo term("bit" fieldTo term("zero"))))
			.assertEqualTo(null)
	}
}