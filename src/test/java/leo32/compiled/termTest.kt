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
		CombineTerm(
			branch(
				BitTerm(one.bit),
				BitTerm(zero.bit))),
		ArgumentTerm)

	val andTerm = SelectTerm(
		SelectTerm(
			CombineTerm(
				branch(
					CombineTerm(
						branch(
							BitTerm(zero.bit),
							BitTerm(zero.bit))),
					CombineTerm(
						branch(
							BitTerm(zero.bit),
							BitTerm(zero.bit))))),
			AtTerm(ArgumentTerm, one.bit)),
		AtTerm(ArgumentTerm, zero.bit))

	val orTerm = InvokeTerm(
		notTerm,
		InvokeTerm(
			andTerm,
			CombineTerm(
				branch(
					InvokeTerm(
						notTerm,
						AtTerm(ArgumentTerm, zero.bit)),
					InvokeTerm(
						notTerm,
						AtTerm(ArgumentTerm, one.bit))))))

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
			.invoke(CombineTerm(branch(BitTerm(zero.bit), BitTerm(zero.bit))))
			.assertEqualTo(BitTerm(zero.bit))

		andTerm
			.invoke(CombineTerm(branch(BitTerm(zero.bit), BitTerm(one.bit))))
			.assertEqualTo(BitTerm(zero.bit))

		andTerm
			.invoke(CombineTerm(branch(BitTerm(one.bit), BitTerm(zero.bit))))
			.assertEqualTo(BitTerm(zero.bit))

		andTerm
			.invoke(CombineTerm(branch(BitTerm(one.bit), BitTerm(one.bit))))
			.assertEqualTo(BitTerm(one.bit))

		andTerm
			.assertFails { invoke(ArgumentTerm) }
	}

	@Test
	fun orTest() {
		orTerm
			.invoke(CombineTerm(branch(BitTerm(zero.bit), BitTerm(zero.bit))))
			.assertEqualTo(BitTerm(zero.bit))

		orTerm
			.invoke(CombineTerm(branch(BitTerm(zero.bit), BitTerm(one.bit))))
			.assertEqualTo(BitTerm(one.bit))

		orTerm
			.invoke(CombineTerm(branch(BitTerm(one.bit), BitTerm(zero.bit))))
			.assertEqualTo(BitTerm(one.bit))

		orTerm
			.invoke(CombineTerm(branch(BitTerm(one.bit), BitTerm(one.bit))))
			.assertEqualTo(BitTerm(one.bit))

		orTerm
			.assertFails { invoke(ArgumentTerm) }
	}
}