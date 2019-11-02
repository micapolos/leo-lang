package leo13.js.compiler

import kotlin.test.Test

class FunctionsCompilerTest {
	@Test
	fun singleFunction() {
		functions(finishCompilation())
			.write(
				script(
					"function" fieldTo script(
						"given" fieldTo script(
							"chicken" fieldTo script()),
						"gives" fieldTo script(
							"egg" fieldTo script()))))
			.write(token(end))
			.assertResult(
				functions(
					types(type("chicken" fieldTo emptyTypes)).gives(nullTyped.plus("egg", nullTyped))))
	}

	@Test
	fun independentFunctions() {
		functions(finishCompilation())
			.write(
				script(
					"function" fieldTo script(
						"given" fieldTo script(
							"chicken" fieldTo script()),
						"gives" fieldTo script(
							"egg" fieldTo script())),
					"function" fieldTo script(
						"given" fieldTo script(
							"horse" fieldTo script()),
						"gives" fieldTo script(
							"shit" fieldTo script()))))
			.write(token(end))
			.assertResult(
				functions(
					types(type("chicken" fieldTo emptyTypes)).gives(nullTyped.plus("egg", nullTyped)),
					types(type("horse" fieldTo emptyTypes)).gives(nullTyped.plus("shit", nullTyped))))
	}

	@Test
	fun dependentFunctions() {
		functions(finishCompilation())
			.write(
				script(
					"function" fieldTo script(
						"given" fieldTo script(
							"chicken" fieldTo script()),
						"gives" fieldTo script(
							"egg" fieldTo script())),
					"function" fieldTo script(
						"given" fieldTo script(
							"farm" fieldTo script()),
						"gives" fieldTo script(
							"chicken" fieldTo script()))))
			.write(token(end))
			.assertResult(
				functions(
					types(type("chicken" fieldTo emptyTypes)).gives(nullTyped.plus("egg", nullTyped)),
					types(type("farm" fieldTo emptyTypes)).gives(nullTyped.plus("chicken", nullTyped))))
	}

}