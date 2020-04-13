package leo14.untyped.typed.lambda

import leo.base.assertEqualTo
import leo14.Script
import leo14.untyped.dsl2.F
import leo14.untyped.dsl2.script_

fun Script.evals_(f: F) =
	eval.assertEqualTo(script_(f))

fun Script.compilesType_(f: F) =
	compileType.assertEqualTo(script_(f))

