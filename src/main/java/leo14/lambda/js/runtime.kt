package leo14.lambda.js

import leo13.js.ast.open
import leo13.js.ast.show
import leo14.Script
import leo14.compile

val Value.open get() = expr.open
val Value.show get() = expr.show

val Script.show
	get() = compiler.compile<Value>(this).show
