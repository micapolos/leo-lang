package leo21.token.evaluator

import leo.base.assertEqualTo
import leo14.lambda.nativeTerm
import leo14.lambda.value.function
import leo14.lambda.value.scope
import leo14.lambda.value.value
import leo14.success
import leo15.dsl.*
import leo21.compiled.compiled
import leo21.evaluated.evaluated
import leo21.evaluated.of
import leo21.prim.Prim
import leo21.prim.prim
import leo21.definition.functionDefinition
import leo21.evaluated.script
import leo21.type.arrowTo
import leo21.type.line
import leo21.type.numberType
import leo21.type.stringType
import leo21.type.type
import kotlin.test.Test

class EvaluateTest {
	@Test
	fun numberPlusNumber() {
		evaluated {
			number(10)
			plus { number(20) }
		}.assertEqualTo(evaluated(30.0))
	}

	@Test
	fun numberText() {
		evaluated { number(123).text }.assertEqualTo(evaluated("123"))
	}

	@Test
	fun function() {
		evaluated {
			function {
				number
				does { text("ok") }
			}
		}.assertEqualTo(
			scope<Prim>()
				.function(nativeTerm(prim("ok")))
				.value
				.of(type(line(numberType arrowTo stringType))))
	}

	@Test
	fun functionApply() {
		evaluated {
			function {
				number
				does { text("ok") }
			}
			apply { number(123) }
		}.assertEqualTo(evaluated("ok"))
	}

	@Test
	fun defineFunction() {
		evaluator {
			define {
				function {
					number
					does { text("ok") }
				}
			}
		}.assertEqualTo(emptyEvaluator.plus(functionDefinition(numberType, compiled("ok"))))
	}

	@Test
	fun stringTryNumber_success() {
		evaluate {
			text("123")
			try_ { number }
		}.assertEqualTo(script_ { try_ { success { number(123) } } })
	}

	@Test
	fun stringTryNumber_failure() {
		evaluate {
			text("123a")
			try_ { number }
		}.assertEqualTo(script_ { try_ { failure } })
	}
}