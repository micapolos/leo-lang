package leo15

import leo.base.assertEqualTo
import leo14.Script
import leo14.untyped.dsl2.F
import leo14.untyped.dsl2.script_

fun Script.compiles(f: F) =
	compile.assertEqualTo(script_(f))

fun Script.evals(f: F) =
	eval.assertEqualTo(script_(f))
