package leo.lab.v2

import leo.*
import leo.base.assertEqualTo
import kotlin.test.Test

class ResolverTest {
	val testPattern = pattern(
		numberWord caseTo pattern(
			zeroWord caseTo pattern(
				end caseTo match(
					pattern(
						incrementWord caseTo pattern(
							end caseTo match(
								pattern(
									recursion(
										sibling.jump)))),
						end caseTo match(
							template(
								doneWord.script)))))))

	@Test
	fun resolver() {
		testPattern
			.resolver
			.assertEqualTo(
				Resolver(
					match(testPattern),
					null))
	}

	@Test
	fun resolve_numberWord() {
		testPattern
			.resolver
			.begin(numberWord)
			.assertEqualTo(
				Resolver(
					match(testPattern.get(numberWord)!!),
					trace(testPattern).plus(parent.jump)))
	}

	@Test
	fun resolve_numberWord_zeroWord() {
		testPattern
			.resolver
			.begin(numberWord)!!
			.begin(zeroWord)
			.assertEqualTo(
				Resolver(
					match(testPattern.get(numberWord)!!.get(zeroWord)!!),
					trace(testPattern)
						.plus(parent.jump)
						.plus(testPattern.get(numberWord)!!)
						.plus(parent.jump)))
	}

	@Test
	fun resolve_numberWord_zeroWord_end() {
		testPattern
			.resolver
			.begin(numberWord)!!
			.begin(zeroWord)!!
			.end
			.assertEqualTo(
				Resolver(
					match(testPattern.get(numberWord)!!.get(zeroWord)!!.endPatternOrNull!!),
					trace(testPattern)
						.plus(parent.jump)
						.plus(testPattern.get(numberWord)!!)
						.plus(sibling.jump)))
	}

	@Test
	fun resolve_numberWord_zeroWord_end_end() {
		testPattern
			.resolver
			.begin(numberWord)!!
			.begin(zeroWord)!!
			.end!!
			.end
			.assertEqualTo(
				Resolver(
					testPattern.get(numberWord)!!.get(zeroWord)!!.endPatternOrNull!!.end!!,
					trace(testPattern)
						.plus(sibling.jump)))
	}

	@Test
	fun resolve_numberWord_zeroWord_end_incrementWord() {
		testPattern
			.resolver
			.begin(numberWord)!!
			.begin(zeroWord)!!
			.end!!
			.begin(incrementWord)
			.assertEqualTo(
				Resolver(
					match(testPattern.get(numberWord)!!.get(zeroWord)!!.endPatternOrNull!!.get(incrementWord)!!),
					trace(testPattern)
						.plus(parent.jump)
						.plus(testPattern.get(numberWord)!!)
						.plus(sibling.jump)
						.plus(testPattern.get(numberWord)!!.get(zeroWord)!!.endPatternOrNull!!)
						.plus(parent.jump)))
	}

	@Test
	fun resolve_numberWord_zeroWord_end_incrementWord_end() {
		testPattern
			.resolver
			.begin(numberWord)!!
			.begin(zeroWord)!!
			.end!!
			.begin(incrementWord)!!
			.end
			.assertEqualTo(
				Resolver(
					match(testPattern.get(numberWord)!!.get(zeroWord)!!.endPatternOrNull!!),
					trace(testPattern)
						.plus(parent.jump)
						.plus(testPattern.get(numberWord)!!)
						.plus(sibling.jump)))
	}

	@Test
	fun resolve_numberWord_zeroWord_end_incrementWord_end_end() {
		testPattern
			.resolver
			.begin(numberWord)!!
			.begin(zeroWord)!!
			.end!!
			.begin(incrementWord)!!
			.end!!
			.end
			.assertEqualTo(
				Resolver(
					testPattern.get(numberWord)!!.get(zeroWord)!!.endPatternOrNull!!.end!!,
					trace(testPattern).plus(sibling.jump)))
	}

	val unitsPattern = pattern(
		unitWord caseTo pattern(
			end caseTo match(
				pattern(
					recursion(sibling.jump)))),
		end caseTo match(
			template(okWord.script)))

	@Test
	fun unitsPatternResolve_unitWord() {
		unitsPattern
			.resolver
			.begin(unitWord)
			.assertEqualTo(
				Resolver(
					match(unitsPattern.get(unitWord)!!),
					trace(unitsPattern).plus(parent.jump)))
	}

	@Test
	fun unitsPatternResolve_unitWord_end() {
		unitsPattern
			.resolver
			.begin(unitWord)!!
			.end
			.assertEqualTo(
				Resolver(
					match(unitsPattern),
					null)
			)
	}

	@Test
	fun unitsPatternResolve_unitWord_end_unitWord() {
		unitsPattern
			.resolver
			.begin(unitWord)!!
			.end!!
			.begin(unitWord)
			.assertEqualTo(
				Resolver(
					match(unitsPattern.get(unitWord)!!),
					trace(unitsPattern).plus(parent.jump)))
	}
}