package leo20.dsl

import kotlin.test.Test

class Test {
	@Test
	fun all() {
		run_(number_)
		run_(get_)
		run_(scope_)
		run_(do__)
		run_(define_)
		run_(with_)
		run_(quote_)
		run_(make_)
		run_(switch_)
		run_(function_)
	}
}