package leo16.library

import leo15.dsl.*
import leo16.value_

val bit = value_ {
	dictionary {
		define { zero.bit.negate.is_ { one.bit } }
		define { one.bit.negate.is_ { zero.bit } }

		define { zero.bit.and { zero.bit }.is_ { zero.bit } }
		define { zero.bit.and { one.bit }.is_ { zero.bit } }
		define { one.bit.and { zero.bit }.is_ { zero.bit } }
		define { one.bit.and { one.bit }.is_ { one.bit } }

		define { zero.bit.or { zero.bit }.is_ { zero.bit } }
		define { zero.bit.or { one.bit }.is_ { one.bit } }
		define { one.bit.or { zero.bit }.is_ { one.bit } }
		define { one.bit.or { one.bit }.is_ { one.bit } }

		define { zero.bit.xor { zero.bit }.is_ { one.bit } }
		define { zero.bit.xor { one.bit }.is_ { zero.bit } }
		define { one.bit.xor { zero.bit }.is_ { zero.bit } }
		define { one.bit.xor { one.bit }.is_ { one.bit } }
	}
}
