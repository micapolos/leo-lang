package leo14.lambda.js

import leo13.js.ast.open
import leo13.js.ast.show
import leo14.Script
import leo14.compile

val Term.open get() = expr.open
val Term.show get() = expr.show

val Script.show
	get() = compiler.compile<Term>(this).show
