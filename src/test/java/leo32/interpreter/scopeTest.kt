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
		val bitTypeTerm = term("bit" to term(
				"either" to term("zero"),
				"either" to term("one")))
		val bitType = bitTypeTerm.parseType

		val types = empty.types
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

		val functions = empty.functions
			.put(bitTypeTerm.plus("nand" to bitTypeTerm).parseType, nandFunction)
			.put(term("not" to bitTypeTerm).parseType,
				function(
					argument,
					op(getter("not")),
					op("nand" to function(
						argument,
						op(getter("not")))),
					op(call(nandFunction))))

		val scope = Scope(types, functions)

		types
			.at(bitZeroTerm.plus("nand" to bitZeroTerm))
			.assertEqualTo(bitTypeTerm.plus("nand" to bitTypeTerm).parseType)

		scope
			.invoke(bitZeroTerm.plus("nand" to bitZeroTerm))
			.assertEqualTo(term("bit" to term("one")))

		scope
			.invoke(bitZeroTerm.plus("nand" to bitOneTerm))
			.assertEqualTo(term("bit" to term("zero")))

		scope
			.invoke(bitOneTerm.plus("nand" to bitZeroTerm))
			.assertEqualTo(term("bit" to term("zero")))

		scope
			.invoke(bitOneTerm.plus("nand" to bitOneTerm))
			.assertEqualTo(term("bit" to term("zero")))

		scope
			.invoke(term("not" to term("bit" to term("zero"))))
			.assertEqualTo(term("bit" to term("one")))
	}
}