package leo16.library

import leo15.dsl.*
import leo16.value_

val ping = value_ {
	dictionary {
		ping.is_ { pong }
	}
}