package leo32.interpreter

import leo.base.assertEqualTo
import leo.base.empty
import leo32.runtime.*
import kotlin.test.Test

class FunctionTest {
	@Test
	fun invoke() {
		val bitZeroTerm = term("bit" to term("zero"))
		val bitOneTerm = term("bit" to term("one"))
		val typeBitTerm = term("type" to term("bit"))
		val bitType = typeBitTerm.type

		val typeResolver = empty.types
			.put(bitZeroTerm, bitType)
			.put(bitOneTerm, bitType)

		val nandFunction = function(
			argument,
			op(
				switch(
					bitZeroTerm.plus("nand" to bitZeroTerm) gives bitOneTerm,
					bitZeroTerm.plus("nand" to bitOneTerm) gives bitZeroTerm,
					bitOneTerm.plus("nand" to bitZeroTerm) gives bitZeroTerm,
					bitOneTerm.plus("nand" to bitOneTerm) gives bitZeroTerm)))

		val templateResolver = empty.templateResolver
			.put(typeBitTerm.plus("nand" to typeBitTerm).type, nandFunction)
			.put(term("not" to typeBitTerm).type,
				function(
					argument,
					op(getter("not")),
					op("nand" to function(
						argument,
						op(getter("not")))),
					op(call(nandFunction))))

		val function = Function(typeResolver, templateResolver)

		typeResolver
			.typeOf(bitZeroTerm.plus("nand" to bitZeroTerm))
			.assertEqualTo(typeBitTerm.plus("nand" to typeBitTerm).type)

		function
			.invoke(bitZeroTerm.plus("nand" to bitZeroTerm))
			.assertEqualTo(term("bit" to term("one")))

		function
			.invoke(bitZeroTerm.plus("nand" to bitOneTerm))
			.assertEqualTo(term("bit" to term("zero")))

		function
			.invoke(bitOneTerm.plus("nand" to bitZeroTerm))
			.assertEqualTo(term("bit" to term("zero")))

		function
			.invoke(bitOneTerm.plus("nand" to bitOneTerm))
			.assertEqualTo(term("bit" to term("zero")))

		function
			.invoke(term("not" to term("bit" to term("zero"))))
			.assertEqualTo(term("bit" to term("one")))
	}
}