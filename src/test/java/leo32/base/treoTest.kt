package leo32.base

import leo.base.assertEqualTo
import leo.binary.bit0
import leo.binary.bit1
import kotlin.test.Test

class TreoTest {
	private val simpleTreo
		get() =
			treo(
				treo(unit),
				treo(
					treo1(treo(unit)),
					treo0(treo(unit))))

	@Test
	fun enterExit() {
		simpleTreo
			.enter(bit0)
			.assertEqualTo(treo(unit))

		simpleTreo
			.enter(bit0)!!
			.enter(bit1)
			.assertEqualTo(null)

		simpleTreo
			.enter(bit1)!!
			.enter(bit0)!!
			.enter(bit1)!!
			.assertEqualTo(treo(unit))

		simpleTreo
			.enter(bit1)!!
			.enter(bit1)!!
			.enter(bit0)!!
			.assertEqualTo(treo(unit))

		simpleTreo
			.enter(bit0)!!
			.exit!!
			.enter(bit1)!!
			.enter(bit1)!!
			.enter(bit0)!!
			.assertEqualTo(treo(unit))

		simpleTreo
			.enter(bit1)!!
			.enter(bit0)!!
			.enter(bit1)!!
			.exit!!
			.exit!!
			.exit
			.assertEqualTo(simpleTreo)

		val treo = simpleTreo
		treo
			.enter(bit1)!!
			.enter(bit1)!!
			.enter(bit0)!!
			.assertEqualTo(treo(unit))

		treo
			.reenter!!
			.reenter!!
			.reenter!!
			.assertEqualTo(treo(unit))
	}

	@Test
	fun invoke() {
		val argument = simpleTreo

		argument
			.enter(bit1)!!
			.enter(bit1)!!
			.enter(bit0)

		simpleTreo
			.invoke(argument)!!
			.assertEqualTo(treo(unit))
	}
}
