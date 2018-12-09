package leo.lab.v2

import leo.*
import leo.base.assertEqualTo
import kotlin.test.Test

class ResolverTest {
	val testFunction = function(
		numberWord caseTo function(
			zeroWord caseTo function(
				end caseTo match(
					function(
						incrementWord caseTo function(
							end caseTo match(
								function(
									recursion(
										sibling.jump)))),
						end caseTo template(okWord.script).body.match)))))

	@Test
	fun resolver() {
		testFunction
			.resolver
			.assertEqualTo(
				Resolver(
					match(testFunction),
					null))
	}

	@Test
	fun resolve_numberWord() {
		testFunction
			.resolver
			.begin(numberWord)
			.assertEqualTo(
				Resolver(
					match(testFunction.get(numberWord)!!),
					trace(testFunction).plus(parent.jump)))
	}

	@Test
	fun resolve_numberWord_zeroWord() {
		testFunction
			.resolver
			.begin(numberWord)!!
			.begin(zeroWord)
			.assertEqualTo(
				Resolver(
					match(testFunction.get(numberWord)!!.get(zeroWord)!!),
					trace(testFunction)
						.plus(parent.jump)
						.plus(testFunction.get(numberWord)!!)
						.plus(parent.jump)))
	}

	@Test
	fun resolve_numberWord_zeroWord_end() {
		testFunction
			.resolver
			.begin(numberWord)!!
			.begin(zeroWord)!!
			.end
			.assertEqualTo(
				Resolver(
					match(testFunction.get(numberWord)!!.get(zeroWord)!!.endFunctionOrNull!!),
					trace(testFunction)
						.plus(parent.jump)
						.plus(testFunction.get(numberWord)!!)
						.plus(sibling.jump)))
	}

	@Test
	fun resolve_numberWord_zeroWord_end_end() {
		testFunction
			.resolver
			.begin(numberWord)!!
			.begin(zeroWord)!!
			.end!!
			.end
			.assertEqualTo(
				Resolver(
					testFunction.get(numberWord)!!.get(zeroWord)!!.endFunctionOrNull!!.end!!,
					trace(testFunction)
						.plus(sibling.jump)))
	}

	@Test
	fun resolve_numberWord_zeroWord_end_incrementWord() {
		testFunction
			.resolver
			.begin(numberWord)!!
			.begin(zeroWord)!!
			.end!!
			.begin(incrementWord)
			.assertEqualTo(
				Resolver(
					match(testFunction.get(numberWord)!!.get(zeroWord)!!.endFunctionOrNull!!.get(incrementWord)!!),
					trace(testFunction)
						.plus(parent.jump)
						.plus(testFunction.get(numberWord)!!)
						.plus(sibling.jump)
						.plus(testFunction.get(numberWord)!!.get(zeroWord)!!.endFunctionOrNull!!)
						.plus(parent.jump)))
	}

	@Test
	fun resolve_numberWord_zeroWord_end_incrementWord_end() {
		testFunction
			.resolver
			.begin(numberWord)!!
			.begin(zeroWord)!!
			.end!!
			.begin(incrementWord)!!
			.end
			.assertEqualTo(
				Resolver(
					match(testFunction.get(numberWord)!!.get(zeroWord)!!.endFunctionOrNull!!),
					trace(testFunction)
						.plus(parent.jump)
						.plus(testFunction.get(numberWord)!!)
						.plus(sibling.jump)))
	}

	@Test
	fun resolve_numberWord_zeroWord_end_incrementWord_end_end() {
		testFunction
			.resolver
			.begin(numberWord)!!
			.begin(zeroWord)!!
			.end!!
			.begin(incrementWord)!!
			.end!!
			.end
			.assertEqualTo(
				Resolver(
					testFunction.get(numberWord)!!.get(zeroWord)!!.endFunctionOrNull!!.end!!,
					trace(testFunction).plus(sibling.jump)))
	}

	val unitsFunction = function(
		unitWord caseTo function(
			end caseTo match(
				function(
					recursion(sibling.jump)))),
		end caseTo template(okWord.script).body.match)

	@Test
	fun unitsPatternResolve_unitWord() {
		unitsFunction
			.resolver
			.begin(unitWord)
			.assertEqualTo(
				Resolver(
					match(unitsFunction.get(unitWord)!!),
					trace(unitsFunction).plus(parent.jump)))
	}

	@Test
	fun unitsPatternResolve_unitWord_end() {
		unitsFunction
			.resolver
			.begin(unitWord)!!
			.end
			.assertEqualTo(
				Resolver(
					match(unitsFunction),
					null)
			)
	}

	@Test
	fun unitsPatternResolve_unitWord_end_unitWord() {
		unitsFunction
			.resolver
			.begin(unitWord)!!
			.end!!
			.begin(unitWord)
			.assertEqualTo(
				Resolver(
					match(unitsFunction.get(unitWord)!!),
					trace(unitsFunction).plus(parent.jump)))
	}
}