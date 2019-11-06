package leo14.typed

import leo13.linkOrNull
import leo14.lambda.Term
import leo14.lambda.pair

fun <T> Term<T>.cast(fromType: Type, toType: Type): Typed<T>? =
	fromType.lineStack.linkOrNull.let { fromLinkOrNull ->
		toType.lineStack.linkOrNull.let { toLinkOrNull ->
			if (fromLinkOrNull == null) {
				if (toLinkOrNull == null) {
					this of emptyType
				} else {
					null
				}
			} else {
				if (toLinkOrNull == null) {
					null
				} else {
					pair().run {
						first.cast(fromLinkOrNull.stack.type, toLinkOrNull.stack.type)?.let { castLhs ->
							second.cast(fromLinkOrNull.value, toLinkOrNull.value)?.let { castRhs ->
								TODO()
							}
						}
					}
				}
			}
		}
	}

fun <T> Term<T>.cast(fromLine: Line, toLine: Line): TypedLine<T>? =
	TODO()