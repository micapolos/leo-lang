package leo14.untyped.dsl

import leo.base.fold
import leo14.untyped.*

fun test_(vararg v: V) =
	emptyValue
		.fold(v) { line ->
			line
				.match("check") { rhs ->
					eval.let { evaled ->
						rhs.eval.let { rhsEvaled ->
							if (evaled != rhsEvaled) error(
								"error" lineTo
									this.plus(
										value(
											"gives" lineTo evaled,
											"expected" lineTo rhsEvaled)))
							else emptyValue
						}
					}
				}
				?: plus(line)
		}
		.run {
			if (!isEmpty) error(
				"error" lineTo value(
					"unchecked" lineTo this))
		}
