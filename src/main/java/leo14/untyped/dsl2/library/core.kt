package leo14.untyped.dsl2.library

import leo14.untyped.dsl2.*

val core = library_ {
	anything.clear.expands { nothing }
	assert { zero.clear.gives { nothing_ } }

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

	anything
	comment { anything }
	expands { given.object_.subject }

	assert {
		number(2)
		comment { text("this is subject") }
		plus {
			number(3)
			comment { text("this is object") }
		}
		comment { text("this is result") }
		gives { number(5) }
	}
}

fun main() = run_(core)