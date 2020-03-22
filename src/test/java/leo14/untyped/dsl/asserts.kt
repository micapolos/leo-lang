package leo14.untyped.dsl

import leo.base.fold
import leo14.untyped.*

fun _test(vararg v: V) =
	emptyProgram
		.fold(v) { value ->
			value
				.match("check") { rhs ->
					eval.let { evaled ->
						rhs.eval.let { rhsEvaled ->
							if (evaled != rhsEvaled) error(
								"error" valueTo
									this.plus(
										program(
											"gives" valueTo evaled,
											"expected" valueTo rhsEvaled)))
							else emptyProgram
						}
					}
				}
				?: plus(value)
		}
		.run {
			if (!isEmpty) error(
				"error" valueTo program(
					"unchecked" valueTo this))
		}
