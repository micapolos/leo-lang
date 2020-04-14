package leo14.untyped.typed.lambda.sandbox

import leo14.untyped.dsl2.*

fun main() {
	main_ {
		boolean.is_ {
			boolean {
				true_
				or { false_ }
			}
		}

		true_.type.is_ { boolean }
		false_.type.is_ { boolean }

		true_.type
	}
}
