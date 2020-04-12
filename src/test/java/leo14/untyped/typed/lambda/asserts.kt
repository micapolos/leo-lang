package leo14.untyped.typed.lambda

import leo.base.assertEqualTo
import leo14.Script
import leo14.untyped.dsl2.F
import leo14.untyped.dsl2.script_

fun Script.gives_(f: F) =
	eval.assertEqualTo(script_(f))

