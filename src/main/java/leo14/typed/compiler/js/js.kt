package leo14.typed.compiler.js

import leo14.Script
import leo14.lambda.js.show

val Script.show
	get() =
		compileTyped.term.show
