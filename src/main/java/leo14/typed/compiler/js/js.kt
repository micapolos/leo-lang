package leo14.typed.compiler.js

import leo14.Script
import leo14.js.ast.open
import leo14.js.ast.show

val Script.show
	get() =
		compileTyped.expr.show

val Script.open
	get() =
		compileTyped.expr.open
