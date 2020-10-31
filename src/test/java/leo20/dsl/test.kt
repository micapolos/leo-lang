package leo20.dsl

import kotlin.test.Test

class Test {
	@Test
	fun all() {
		run_(number_)
		run_(text_)
		run_(get_)
		run_(scope_)
		run_(save_)
		run_(do_)
		run_(define_)
		run_(resolve_)
		run_(content_)
		run_(quote_)
		run_(make_)
		run_(switch_)
		run_(function_)

		run_(list_)
	}
}