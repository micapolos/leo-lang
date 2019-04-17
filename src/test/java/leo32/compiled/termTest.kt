package leo32.compiled

import leo.base.assertEqualTo
import leo.base.assertFails
import leo.binary.bit
import leo.binary.one
import leo.binary.zero
import leo32.base.branch
import kotlin.test.Test

class TermTest {
	val notTerm = SelectTerm(
		BranchTerm(
			branch(
				BitTerm(one.bit),
				BitTerm(zero.bit))),
		ArgumentTerm)

	val andTerm = SelectTerm(
		SelectTerm(
			BranchTerm(
				branch(
					BranchTerm(
						branch(
							BitTerm(zero.bit),
							BitTerm(zero.bit))),
					BranchTerm(
						branch(
							BitTerm(zero.bit),
							BitTerm(one.bit))))),
			SelectTerm(ArgumentTerm, BitTerm(one.bit))),
		SelectTerm(ArgumentTerm, BitTerm(zero.bit)))

	val orTerm = InvokeTerm(
		notTerm,
		InvokeTerm(
			andTerm,
			BranchTerm(
				branch(
					InvokeTerm(
						notTerm,
						SelectTerm(ArgumentTerm, BitTerm(zero.bit))),
					InvokeTerm(
						notTerm,
						SelectTerm(ArgumentTerm, BitTerm(one.bit)))))))

	@Test
	fun not() {
		notTerm
			.invoke(BitTerm(zero.bit))
			.assertEqualTo(BitTerm(one.bit))

		notTerm
			.invoke(BitTerm(one.bit))
			.assertEqualTo(BitTerm(zero.bit))

		notTerm
			.assertFails { invoke(ArgumentTerm) }
	}

	@Test
	fun and() {
		andTerm
			.invoke(BranchTerm(branch(BitTerm(zero.bit), BitTerm(zero.bit))))
			.assertEqualTo(BitTerm(zero.bit))

		andTerm
			.invoke(BranchTerm(branch(BitTerm(zero.bit), BitTerm(one.bit))))
			.assertEqualTo(BitTerm(zero.bit))

		andTerm
			.invoke(BranchTerm(branch(BitTerm(one.bit), BitTerm(zero.bit))))
			.assertEqualTo(BitTerm(zero.bit))

		andTerm
			.invoke(BranchTerm(branch(BitTerm(one.bit), BitTerm(one.bit))))
			.assertEqualTo(BitTerm(one.bit))

		andTerm
			.assertFails { invoke(ArgumentTerm) }
	}

	@Test
	fun orTest() {
		orTerm
			.invoke(BranchTerm(branch(BitTerm(zero.bit), BitTerm(zero.bit))))
			.assertEqualTo(BitTerm(zero.bit))

		orTerm
			.invoke(BranchTerm(branch(BitTerm(zero.bit), BitTerm(one.bit))))
			.assertEqualTo(BitTerm(one.bit))

		orTerm
			.invoke(BranchTerm(branch(BitTerm(one.bit), BitTerm(zero.bit))))
			.assertEqualTo(BitTerm(one.bit))

		orTerm
			.invoke(BranchTerm(branch(BitTerm(one.bit), BitTerm(one.bit))))
			.assertEqualTo(BitTerm(one.bit))

		orTerm
			.assertFails { invoke(ArgumentTerm) }
	}
}