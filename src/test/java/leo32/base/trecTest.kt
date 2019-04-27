package leo32.base

import leo.base.assertEqualTo
import kotlin.test.Test

class TrecTest {
	private val simpleTrec
		get() =
			trec(
				at0(trec(leaf(1))),
				at1(trec(
					at0(trec(
						at1(trec(leaf(2))))),
					at1(trec(
						at0(trec(leaf(3))))))))

	@Test
	fun simple() {
		simpleTrec
			.enter0!!
			.leafOrNull()
			.assertEqualTo(1)

		simpleTrec
			.enter0!!
			.exit!!
			.assertEqualTo(simpleTrec)

		simpleTrec
			.enter1!!
			.enter0!!
			.enter1!!
			.leafOrNull()
			.assertEqualTo(2)

		simpleTrec
			.enter1!!
			.enter1!!
			.enter0!!
			.leafOrNull()
			.assertEqualTo(3)

		simpleTrec
			.enter0!!
			.enter1
			.assertEqualTo(null)
	}

	private val zerosTrec
		get() =
			trec(at0(trec<Unit>(rec)))

	@Test
	fun zerosRec() {
		zerosTrec.enter0.assertEqualTo(zerosTrec)
		zerosTrec.enter1.assertEqualTo(null)
		zerosTrec.exit.assertEqualTo(null)
		zerosTrec.enter0!!.exit.assertEqualTo(null)
	}

	private val onesAndZeroTrec
		get() =
			trec(
				at0(trec(leaf("ok"))),
				at1(trec(rec)))

	@Test
	fun onesAndZero() {
		onesAndZeroTrec.enter0!!.leafOrNull().assertEqualTo("ok")
		onesAndZeroTrec.enter1!!.enter0!!.leafOrNull().assertEqualTo("ok")
		onesAndZeroTrec.enter1!!.enter1!!.enter0!!.leafOrNull().assertEqualTo("ok")
		onesAndZeroTrec.enter1!!.enter1!!.enter1!!.enter0!!.leafOrNull().assertEqualTo("ok")
	}
}
