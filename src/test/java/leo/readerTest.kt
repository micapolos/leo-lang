package leo

fun <V> identityEvaluator(applyFn: (Term<Nothing>) -> Match?): Evaluator<V> =
	Evaluator(
		{ it -> identityEvaluator(applyFn) },
		applyFn,
		{ null })

class ReaderTest {
//	@Test
//	fun read_mismatch() {
//		Reader(
//			Field<Nothing>::parseBit,
//			Bit::reflect,
//			identityEvaluator {
//				term -> null
//			},
//			null)
//			.read(Bit.ZERO)!!
//			.termOrNull
//			.assertEqualTo(null)
//	}
//
//	@Test
//	fun read_partial() {
//		Reader(
//			Field<Nothing>::parseBit,
//			Bit::reflect,
//			identityEvaluator {
//				term -> Match<V>?
//			},
//			null)
//			.read(Bit.ZERO)!!
//			.termOrNull
//			.assertEqualTo(null)
//	}
}