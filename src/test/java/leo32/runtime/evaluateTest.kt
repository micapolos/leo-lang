package leo32.runtime

import leo.base.assertEqualTo
import org.junit.Test

class EvaluateTest {
	@Test
	fun empty() {
		script().evaluate.assertEqualTo(script())
	}

	@Test
	fun one() {
		script(oneSymbol).evaluate.assertEqualTo(script(oneSymbol))
	}

	@Test
	fun one__two() {
		script(
			oneSymbol to script(),
			twoSymbol to script())
			.evaluate
			.assertEqualTo(script(twoSymbol to script(oneSymbol)))
	}

	@Test
	fun one_two() {
		script(oneSymbol to script(twoSymbol))
			.evaluate
			.assertEqualTo(script(oneSymbol to script(twoSymbol)))
	}

	@Test
	fun one_two___three() {
		script(
			oneSymbol to script(twoSymbol),
			threeSymbol to script())
			.evaluate
			.assertEqualTo(
				script(
					threeSymbol to script(
						oneSymbol to script(twoSymbol))))
	}

	@Test
	fun one_two___three_four() {
		script(
			oneSymbol to script(twoSymbol),
			threeSymbol to script(fourSymbol))
			.evaluate
			.assertEqualTo(
				script(
					oneSymbol to script(twoSymbol),
					threeSymbol to script(fourSymbol)))
	}

	@Test
	fun one__one() {
		script(
			oneSymbol to script(),
			oneSymbol to script())
			.evaluate
			.assertEqualTo(script())
	}

	@Test
	fun one_foo___one_bar() {
		script(
			oneSymbol to script(zeroSymbol),
			oneSymbol to script(oneSymbol))
			.evaluate
			.assertEqualTo(
				script(
					oneSymbol to script(zeroSymbol),
					oneSymbol to script(oneSymbol)))
	}

	@Test
	fun one_two___one() {
		script(
			oneSymbol to script(twoSymbol),
			oneSymbol to script())
			.evaluate
			.assertEqualTo(script(twoSymbol))
	}

	@Test
	fun one_two___two_three___one() {
		script(
			oneSymbol to script(twoSymbol),
			twoSymbol to script(threeSymbol),
			oneSymbol to script())
			.evaluate
			.assertEqualTo(script(twoSymbol))
	}

	@Test
	fun one_two___two_three___two() {
		script(
			oneSymbol to script(twoSymbol),
			twoSymbol to script(threeSymbol),
			twoSymbol to script())
			.evaluate
			.assertEqualTo(script(threeSymbol))
	}

	@Test
	fun one_two___two_three___three() {
		script(
			oneSymbol to script(twoSymbol),
			twoSymbol to script(threeSymbol),
			threeSymbol to script())
			.evaluate
			.assertEqualTo(
				script(
					threeSymbol to script(
						oneSymbol to script(twoSymbol),
						twoSymbol to script(threeSymbol))))
	}

	@Test
	fun equals() {
		script(equalsSymbol)
			.evaluate
			.assertEqualTo(script(booleanSymbol to script(trueSymbol)))
	}

	@Test
	fun one__equals_one() {
		script(
			oneSymbol to script(),
			equalsSymbol to script(oneSymbol))
			.evaluate
			.assertEqualTo(script(booleanSymbol to script(trueSymbol)))
	}

	@Test
	fun one__equals_two() {
		script(
			oneSymbol to script(),
			equalsSymbol to script(twoSymbol))
			.evaluate
			.assertEqualTo(script(booleanSymbol to script(falseSymbol)))
	}

	@Test
	fun it_one__equals_two() {
		script(
			itSymbol to script(
				oneSymbol to script(),
				equalsSymbol to script(twoSymbol)))
			.evaluate
			.assertEqualTo(script(itSymbol to script(booleanSymbol to script(falseSymbol))))
	}

	@Test
	fun quote() {
		script(quoteSymbol)
			.evaluate
			.assertEqualTo(script())
	}

	@Test
	fun quote_one() {
		script(quoteSymbol to script(oneSymbol))
			.evaluate
			.assertEqualTo(script(oneSymbol))
	}

	@Test
	fun quote_one__two() {
		script(quoteSymbol to script(
			oneSymbol to script(),
			twoSymbol to script()))
			.evaluate
			.assertEqualTo(
				script(
					oneSymbol to script(),
					twoSymbol to script()))
	}

	@Test
	fun quote_one__equals_two() {
		script(
			quoteSymbol to script(
				oneSymbol to script(),
				equalsSymbol to script(twoSymbol)))
			.evaluate
			.assertEqualTo(
				script(
					oneSymbol to script(),
					equalsSymbol to script(twoSymbol)))
	}

	@Test
	fun quote_one__equals_two___evaluate() {
		script(
			quoteSymbol to script(
				oneSymbol to script(),
				equalsSymbol to script(twoSymbol)),
			unquoteSymbol to script())
			.evaluate
			.assertEqualTo(
				script(booleanSymbol to script(falseSymbol)))
	}

	@Test
	fun it_quote() {
		script(itSymbol to script(quoteSymbol))
			.evaluate
			.assertEqualTo(script(itSymbol))
	}

	@Test
	fun it_quote_one() {
		script(itSymbol to script(quoteSymbol to script(oneSymbol)))
			.evaluate
			.assertEqualTo(script(itSymbol to script(oneSymbol)))
	}

	@Test
	fun quote_quote() {
		script(quoteSymbol to script(quoteSymbol))
			.evaluate
			.assertEqualTo(script(quoteSymbol))
	}

	@Test
	fun error() {
		script(errorSymbol)
			.evaluate
			.assertEqualTo(script(errorSymbol))
	}

	@Test
	fun error_one() {
		script(errorSymbol to script(oneSymbol))
			.evaluate
			.assertEqualTo(script(errorSymbol to script(oneSymbol)))
	}

	@Test
	fun error_one__two() {
		script(
			errorSymbol to script(oneSymbol),
			twoSymbol to script())
			.evaluate
			.assertEqualTo(script(errorSymbol to script(oneSymbol)))
	}

	@Test
	fun foo__has_bar() {
		script(
			zeroSymbol to script(),
			hasSymbol to script(oneSymbol))
			.evaluate
			.assertEqualTo(script())
	}

	@Test
	fun foo__has_bar__foo() {
		script(
			zeroSymbol to script(),
			hasSymbol to script(oneSymbol),
			zeroSymbol to script())
			.evaluate
			.assertEqualTo(script(zeroSymbol))
	}

	@Test
	fun foo__has_bar__class_foo_bar() {
		script(
			zeroSymbol to script(),
			hasSymbol to script(oneSymbol),
			classSymbol to script(zeroSymbol to script(oneSymbol to script())))
			.evaluate
			.assertEqualTo(script(zeroSymbol))
	}

	@Test
	fun foo__has_bar__describe_foo() {
		script(
			zeroSymbol to script(),
			hasSymbol to script(oneSymbol),
			describeSymbol to script(zeroSymbol))
			.evaluate
			.assertEqualTo(script(zeroSymbol to script(oneSymbol)))
	}

	@Test
	fun foo_zoo___has_bar__foo_zoo() {
		script(
			zeroSymbol to script(fourSymbol to script()),
			hasSymbol to script(oneSymbol),
			zeroSymbol to script(fourSymbol))
			.evaluate
			.assertEqualTo(script(zeroSymbol to script(fourSymbol)))
	}

	@Test
	fun foo_zoo___has_bar__class_foo_zoo_bar() {
		script(
			zeroSymbol to script(fourSymbol to script()),
			hasSymbol to script(oneSymbol),
			classSymbol to script(zeroSymbol to script(fourSymbol to script(oneSymbol))))
			.evaluate
			.assertEqualTo(script(zeroSymbol to script(fourSymbol)))
	}

	@Test
	fun foo_zoo___has_bar__describe_foo_zoo() {
		script(
			zeroSymbol to script(fourSymbol to script()),
			hasSymbol to script(oneSymbol),
			describeSymbol to script(zeroSymbol to script(fourSymbol)))
			.evaluate
			.assertEqualTo(script(zeroSymbol to script(fourSymbol to script(oneSymbol))))
	}

	@Test
	fun bit__has_either_zero___either_one___bit() {
		script(
			bitSymbol to script(),
			hasSymbol to script(
				eitherSymbol to script(zeroSymbol),
				eitherSymbol to script(oneSymbol)),
			bitSymbol to script())
			.evaluate
			.assertEqualTo(script(bitSymbol))
	}

	@Test
	fun bit__has_either_zero___either_one____class_bit_zero() {
		script(
			bitSymbol to script(),
			hasSymbol to script(
				eitherSymbol to script(zeroSymbol),
				eitherSymbol to script(oneSymbol)),
			classSymbol to script(bitSymbol to script(zeroSymbol)))
			.evaluate
			.assertEqualTo(script(bitSymbol))
	}

	@Test
	fun bit__has_either_zero___either_one____describe_bit() {
		script(
			bitSymbol to script(),
			hasSymbol to script(
				eitherSymbol to script(zeroSymbol),
				eitherSymbol to script(oneSymbol)),
			describeSymbol to script(bitSymbol))
			.evaluate
			.assertEqualTo(
				script(bitSymbol to script(
					eitherSymbol to script(zeroSymbol),
					eitherSymbol to script(oneSymbol))))
	}

	@Test
	fun bit__has_either_zero___either_one____class_bit_one() {
		script(
			bitSymbol to script(),
			hasSymbol to script(
				eitherSymbol to script(zeroSymbol),
				eitherSymbol to script(oneSymbol)),
			classSymbol to script(bitSymbol to script(oneSymbol)))
			.evaluate
			.assertEqualTo(script(bitSymbol))
	}

	@Test
	fun zero__gives_one() {
		script(
			zeroSymbol to script(),
			givesSymbol to script(oneSymbol))
			.evaluate
			.assertEqualTo(script())
	}

	@Test
	fun zero__gives_one___zero() {
		script(
			zeroSymbol to script(),
			givesSymbol to script(oneSymbol),
			zeroSymbol to script())
			.evaluate
			.assertEqualTo(script(oneSymbol))
	}
}
