package leo14.lambda.js

import leo14.Script
import leo14.compile
import leo14.js.ast.open
import leo14.js.ast.show

val Term.open get() = expr.open
val Term.show get() = expr.show

val Script.show
	get() = compiler.compile<Term>(this).show
