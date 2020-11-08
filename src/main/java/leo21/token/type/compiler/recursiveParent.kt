package leo21.token.type.compiler

import leo21.token.processor.TokenProcessor
import leo21.type.Recursive

sealed class RecursiveParent

fun RecursiveParent.plus(recursive: Recursive): TokenProcessor =
	TODO()