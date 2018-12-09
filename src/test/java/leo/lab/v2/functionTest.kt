package leo.lab.v2

import leo.*
import leo.base.assertEqualTo
import kotlin.test.Test

class FunctionTest {
	@Test
	fun wordFunction() {
		val trueOrFalseFunction = function(
			trueWord caseTo function(
				end caseTo template(oneWord.script).body.match),
			falseWord caseTo function(
				end caseTo template(twoWord.script).body.match))

		trueOrFalseFunction.invoke(trueWord.script).assertEqualTo(oneWord.script)
		trueOrFalseFunction.invoke(falseWord.script).assertEqualTo(twoWord.script)
		trueOrFalseFunction.invoke(zeroWord.script).assertEqualTo(zeroWord.script)
	}

	@Test
	fun rhsFunction() {
		val trueOrFalseFunction = function(
			booleanWord caseTo function(
				trueWord caseTo function(
					end caseTo match(
						function(
							end caseTo template(oneWord.script).body.match))),
				falseWord caseTo function(
					end caseTo match(
						function(
							end caseTo template(twoWord.script).body.match)))))

		trueOrFalseFunction
			.invoke(script(booleanWord to trueWord.script))
			.assertEqualTo(oneWord.script)
		trueOrFalseFunction
			.invoke(script(booleanWord to falseWord.script))
			.assertEqualTo(twoWord.script)
		trueOrFalseFunction
			.invoke(script(booleanWord to oneWord.script))
			.assertEqualTo(script(booleanWord to oneWord.script))
		trueOrFalseFunction
			.invoke(booleanWord.script)
			.assertEqualTo(booleanWord.script)
	}

	@Test
	fun lhsFunction() {
		val trueOrFalseFunction = function(
			trueWord caseTo function(
				end caseTo match(
					function(
						negateWord caseTo function(
							end caseTo template(oneWord.script).body.match)))),
			falseWord caseTo function(
				end caseTo match(
					function(
						negateWord caseTo function(
							end caseTo template(twoWord.script).body.match)))))

		trueOrFalseFunction
			.invoke(script(trueWord to null, negateWord to null))
			.assertEqualTo(oneWord.script)
		trueOrFalseFunction
			.invoke(script(falseWord to null, negateWord to null))
			.assertEqualTo(twoWord.script)
		trueOrFalseFunction
			.invoke(trueWord.script)
			.assertEqualTo(trueWord.script)
	}

	@Test
	fun siblingRecursion() {
		val pattern = function(
			thisWord caseTo function(
				unitWord caseTo function(
					end caseTo match(
						function(
							recursion(sibling.jump)))),
				end caseTo template(okWord.script).body.match))

		pattern
			.invoke(script(thisWord to null))
			.assertEqualTo(okWord.script)

		pattern
			.invoke(script(thisWord to script(unitWord to null)))
			.assertEqualTo(okWord.script)

		pattern
			.invoke(script(thisWord to script(unitWord to null, unitWord to null)))
			.assertEqualTo(okWord.script)
	}

	@Test
	fun siblingRecursion_withTail() {
		val pattern = function(
			numberWord caseTo function(
				zeroWord caseTo function(
					end caseTo match(
						function(
							incrementWord caseTo function(
								end caseTo match(
									function(
										recursion(sibling.jump)))),
							end caseTo template(okWord.script).body.match)))))

		pattern
			.invoke(
				script(
					numberWord to script(
						zeroWord to null)))
			.assertEqualTo(okWord.script)

		pattern
			.invoke(
				script(
					numberWord to script(
						zeroWord to null,
						incrementWord to null)))
			.assertEqualTo(okWord.script)
	}
}
