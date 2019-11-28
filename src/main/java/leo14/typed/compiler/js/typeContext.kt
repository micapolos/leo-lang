package leo14.typed.compiler.js

import leo14.typed.compiler.typeContext
import leo14.typed.numberLineNative
import leo14.typed.textLineNative

val jsTypeContext = typeContext(
	textLineNative,
	numberLineNative,
	objectLineNative)