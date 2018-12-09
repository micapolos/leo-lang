package leo.lab.v2

import leo.base.assertEqualTo
import leo.incrementWord
import leo.numberWord
import leo.unitWord
import leo.zeroWord
import kotlin.test.Test

class ResolverTest {
	@Test
	fun resolver() {
		numberFunction
			.resolver
			.assertEqualTo(
				Resolver(
					match(numberFunction),
					null))
	}

	@Test
	fun resolve_numberWord() {
		numberFunction
			.resolver
			.begin(numberWord)
			.assertEqualTo(
				Resolver(
					match(numberFunction.get(numberWord)!!),
					trace(numberFunction).plus(parent.jump)))
	}

	@Test
	fun resolve_numberWord_zeroWord() {
		numberFunction
			.resolver
			.begin(numberWord)!!
			.begin(zeroWord)
			.assertEqualTo(
				Resolver(
					match(numberFunction.get(numberWord)!!.get(zeroWord)!!),
					trace(numberFunction)
						.plus(parent.jump)
						.plus(numberFunction.get(numberWord)!!)
						.plus(parent.jump)))
	}

	@Test
	fun resolve_numberWord_zeroWord_end() {
		numberFunction
			.resolver
			.begin(numberWord)!!
			.begin(zeroWord)!!
			.end
			.assertEqualTo(
				Resolver(
					match(numberFunction.get(numberWord)!!.get(zeroWord)!!.endFunctionOrNull!!),
					trace(numberFunction)
						.plus(parent.jump)
						.plus(numberFunction.get(numberWord)!!)
						.plus(sibling.jump)))
	}

	@Test
	fun resolve_numberWord_zeroWord_end_end() {
		numberFunction
			.resolver
			.begin(numberWord)!!
			.begin(zeroWord)!!
			.end!!
			.end
			.assertEqualTo(
				Resolver(
					numberFunction.get(numberWord)!!.get(zeroWord)!!.endFunctionOrNull!!.end!!,
					trace(numberFunction)
						.plus(sibling.jump)))
	}

	@Test
	fun resolve_numberWord_zeroWord_end_incrementWord() {
		numberFunction
			.resolver
			.begin(numberWord)!!
			.begin(zeroWord)!!
			.end!!
			.begin(incrementWord)
			.assertEqualTo(
				Resolver(
					match(numberFunction.get(numberWord)!!.get(zeroWord)!!.endFunctionOrNull!!.get(incrementWord)!!),
					trace(numberFunction)
						.plus(parent.jump)
						.plus(numberFunction.get(numberWord)!!)
						.plus(sibling.jump)
						.plus(numberFunction.get(numberWord)!!.get(zeroWord)!!.endFunctionOrNull!!)
						.plus(parent.jump)))
	}

	@Test
	fun resolve_numberWord_zeroWord_end_incrementWord_end() {
		numberFunction
			.resolver
			.begin(numberWord)!!
			.begin(zeroWord)!!
			.end!!
			.begin(incrementWord)!!
			.end
			.assertEqualTo(
				Resolver(
					match(numberFunction.get(numberWord)!!.get(zeroWord)!!.endFunctionOrNull!!),
					trace(numberFunction)
						.plus(parent.jump)
						.plus(numberFunction.get(numberWord)!!)
						.plus(sibling.jump)))
	}

	@Test
	fun resolve_numberWord_zeroWord_end_incrementWord_end_end() {
		numberFunction
			.resolver
			.begin(numberWord)!!
			.begin(zeroWord)!!
			.end!!
			.begin(incrementWord)!!
			.end!!
			.end
			.assertEqualTo(
				Resolver(
					numberFunction.get(numberWord)!!.get(zeroWord)!!.endFunctionOrNull!!.end!!,
					trace(numberFunction).plus(sibling.jump)))
	}

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