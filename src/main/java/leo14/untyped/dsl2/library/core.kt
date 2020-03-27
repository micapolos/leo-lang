package leo14.untyped.dsl2.library

import leo14.untyped.dsl2.*

val core = library_ {
	nothing.gives { nothing_ }
	assert { nothing.gives { nothing_ } }

	anything.clear.gives { nothing }
	assert { zero.clear.gives { nothing } }

	anything
	replace { anything }
	does { given.replace.object_ }
	assert { zero.replace { one }.gives { one } }

	if_ { anything }
	then_ { anything }
	else_ { anything }
	expands {
		given.if_.object_
		match {
			true_ { given.then_.object_ }
			false_ { given.else_.object_ }
		}
	}

	assert {
		if_ { number(1).equals_ { number(1) } }
		then_ { text("ok") }
		else_ { text("not ok") }
		gives { text("ok") }
	}

	assert {
		if_ { number(1).equals_ { number(2) } }
		then_ { text("ok") }
		else_ { text("not ok") }
		gives { text("not ok") }
	}
}

fun main() = run_(core)