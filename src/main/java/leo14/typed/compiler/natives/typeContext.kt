package leo14.typed.compiler.natives

import leo14.typed.compiler.typeContext
import leo14.typed.numberLineNative
import leo14.typed.textLineNative

val nativeTypeContext = typeContext(textLineNative, numberLineNative)