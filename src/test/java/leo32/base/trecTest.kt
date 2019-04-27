package leo32.base

import leo.base.assertEqualTo
import kotlin.test.Test

class TrecTest {
	@Test
	fun enterExit() {
		val trec =
			trec(
				at0(trec(leaf(1))),
				at1(trec(
					at0(trec(
						at1(trec(leaf(2))))),
					at1(trec(
						at0(trec(leaf(3))))))))

		trec
			.enter0!!
			.leafOrNull()
			.assertEqualTo(1)

		trec
			.enter0!!
			.exit!!
			.assertEqualTo(trec)

		trec
			.enter1!!
			.enter0!!
			.enter1!!
			.leafOrNull()
			.assertEqualTo(2)

		trec
			.enter1!!
			.enter1!!
			.enter0!!
			.leafOrNull()
			.assertEqualTo(3)

		trec
			.enter0!!
			.enter1
			.assertEqualTo(null)
	}
}