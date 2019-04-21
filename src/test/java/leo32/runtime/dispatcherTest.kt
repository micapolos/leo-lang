package leo32.runtime

import leo.base.assertEqualTo
import leo.base.empty
import kotlin.test.Test

class DispatcherTest {
	@Test
	fun simple() {
		empty
			.dispatcher
			.put(term(pingSymbol) caseTo term(pongSymbol))
			.put(term(pongSymbol) caseTo term(pingSymbol))
			.apply { at(term(pingSymbol)).assertEqualTo(term(pongSymbol)) }
			.apply { at(term(pongSymbol)).assertEqualTo(term(pingSymbol)) }
			.apply { at(term(zeroSymbol)).assertEqualTo(null) }
	}

	@Test
	fun field() {
		empty
			.dispatcher
			.put(term(negateSymbol to term(zeroSymbol)) caseTo term(oneSymbol))
			.put(term(negateSymbol to term(oneSymbol)) caseTo term(zeroSymbol))
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
					ySymbol to term(oneSymbol)) caseTo term(selfSymbol))
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
					eitherSymbol to term(oneSymbol)) caseTo term(selfSymbol))
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
							eitherSymbol to term(oneSymbol)))) caseTo term(selfSymbol))
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
							eitherSymbol to term("false")))) caseTo
					term(selfSymbol))
			.apply { at(term(bitSymbol to term(zeroSymbol))).assertEqualTo(term(selfSymbol)) }
			.apply { at(term(bitSymbol to term(oneSymbol))).assertEqualTo(term(selfSymbol)) }
			.apply { at(term(bitSymbol to term(twoSymbol))).assertEqualTo(null) }
			.apply { at(term(booleanSymbol to term(falseSymbol))).assertEqualTo(term(selfSymbol)) }
			.apply { at(term(booleanSymbol to term(trueSymbol))).assertEqualTo(term(selfSymbol)) }
			.apply { at(term(booleanSymbol to term(maybeSymbol))).assertEqualTo(null) }
	}
}