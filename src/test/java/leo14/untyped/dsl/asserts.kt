package leo14.untyped.dsl

import leo.base.fold
import leo14.untyped.*

fun test_(vararg v: V) =
	emptyProgram
		.fold(v) { line ->
			line
				.match("check") { rhs ->
					eval.let { evaled ->
						rhs.eval.let { rhsEvaled ->
							if (evaled != rhsEvaled) error(
								"error" lineTo
									this.plus(
										program(
											"gives" lineTo evaled,
											"expected" lineTo rhsEvaled)))
							else emptyProgram
						}
					}
				}
				?: plus(line)
		}
		.run {
			if (!isEmpty) error(
				"error" lineTo program(
					"unchecked" lineTo this))
		}
