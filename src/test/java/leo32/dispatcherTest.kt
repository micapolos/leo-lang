package leo32

import leo.base.assertEqualTo
import leo.base.empty
import kotlin.test.Test

class DispatcherTest {
	@Test
	fun simple() {
		empty
			.dispatcher
			.put(term(pingSymbol) gives term(pongSymbol))
			.put(term(pongSymbol) gives term(pingSymbol))
			.apply { at(term(pingSymbol)).assertEqualTo(term(pongSymbol)) }
			.apply { at(term(pongSymbol)).assertEqualTo(term(pingSymbol)) }
			.apply { at(term(zeroSymbol)).assertEqualTo(null) }
	}

	@Test
	fun field() {
		empty
			.dispatcher
			.put(term(negateSymbol to term(zeroSymbol)) gives term(oneSymbol))
			.put(term(negateSymbol to term(oneSymbol)) gives term(zeroSymbol))
			.apply { at(term(negateSymbol to term(zeroSymbol))).assertEqualTo(term(oneSymbol)) }
			.apply { at(term(negateSymbol to term(oneSymbol))).assertEqualTo(term(zeroSymbol)) }
	}

	@Test
	fun fields() {
		empty
			.dispatcher
			.put(
				term(
					xSymbol to term(zeroSymbol),
					ySymbol to term(oneSymbol)) gives term(selfSymbol))
			.apply {
				at(
					term(
						xSymbol to term(zeroSymbol),
						ySymbol to term(oneSymbol)))
					.assertEqualTo(term(selfSymbol))
			}
			.apply {
				at(
					term(
						xSymbol to term(zeroSymbol),
						zSymbol to term(oneSymbol)))
					.assertEqualTo(null)
			}
			.apply { at(term(xSymbol to term(zeroSymbol))).assertEqualTo(null) }
	}

	@Test
	fun alternatives() {
		empty
			.dispatcher
			.put(
				term(
					eitherSymbol to term(zeroSymbol),
					eitherSymbol to term(oneSymbol)) gives term(selfSymbol))
			.apply { at(term(zeroSymbol)).assertEqualTo(term(selfSymbol)) }
			.apply { at(term(oneSymbol)).assertEqualTo(term(selfSymbol)) }
			.apply { at(term(twoSymbol)).assertEqualTo(null) }
	}

	@Test
	fun independentAlternatives() {
		empty
			.dispatcher
			.put(
				term(
					bitSymbol to term(
						eitherSymbol to term(zeroSymbol),
						eitherSymbol to term(oneSymbol)),
					andSymbol to term(
						bitSymbol to term(
							eitherSymbol to term(zeroSymbol),
							eitherSymbol to term(oneSymbol)))) gives term(selfSymbol))
			.apply {
				at(
					term(
						bitSymbol to term(zeroSymbol),
						andSymbol to term(
							bitSymbol to term(zeroSymbol)))).assertEqualTo(term(selfSymbol))
			}
			.apply {
				at(
					term(
						bitSymbol to term(zeroSymbol),
						andSymbol to term(
							bitSymbol to term(oneSymbol)))).assertEqualTo(term(selfSymbol))
			}
			.apply {
				at(
					term(
						bitSymbol to term(oneSymbol),
						andSymbol to term(
							bitSymbol to term(zeroSymbol)))).assertEqualTo(term(selfSymbol))
			}
			.apply {
				at(
					term(
						bitSymbol to term(oneSymbol),
						andSymbol to term(
							bitSymbol to term(oneSymbol)))).assertEqualTo(term(selfSymbol))
			}
			.apply {
				at(
					term(
						bitSymbol to term(zeroSymbol),
						andSymbol to term(
							bitSymbol to term(twoSymbol)))).assertEqualTo(null)
			}
	}

	@Test
	fun dependentAlternatives() {
		empty
			.dispatcher
			.put(
				term(
					eitherSymbol to term(
						bitSymbol to term(
							eitherSymbol to term("zero"),
							eitherSymbol to term("one"))),
					eitherSymbol to term(
						booleanSymbol to term(
							eitherSymbol to term("true"),
							eitherSymbol to term("false")))) gives
					term(selfSymbol))
			.apply { at(term(bitSymbol to term(zeroSymbol))).assertEqualTo(term(selfSymbol)) }
			.apply { at(term(bitSymbol to term(oneSymbol))).assertEqualTo(term(selfSymbol)) }
			.apply { at(term(bitSymbol to term(twoSymbol))).assertEqualTo(null) }
			.apply { at(term(booleanSymbol to term(falseSymbol))).assertEqualTo(term(selfSymbol)) }
			.apply { at(term(booleanSymbol to term(trueSymbol))).assertEqualTo(term(selfSymbol)) }
			.apply { at(term(booleanSymbol to term(maybeSymbol))).assertEqualTo(null) }
	}

	@Test
	fun int() {
		val size = 32
		empty
			.dispatcher
			.put(
				term(intSymbol to term(size,
					bitSymbol to term(
						eitherSymbol to term(zeroSymbol),
						eitherSymbol to term(oneSymbol)))) gives
					term(intSymbol))
			.apply {
				at(term(intSymbol to term(size, bitSymbol to term(zeroSymbol)))).assertEqualTo(term(intSymbol))
				at(term(intSymbol to term(size, bitSymbol to term(oneSymbol)))).assertEqualTo(term(intSymbol))
				at(term(intSymbol to term(size, bitSymbol to term(twoSymbol)))).assertEqualTo(null)
			}
	}
}