package leo13.untyped.compiler

import leo9.Stack
import leo9.push
import leo9.stack

data class CompiledSet(val lineStack: Stack<CompiledLine>)

fun compiledSet() = CompiledSet(stack())

fun CompiledSet.plus(line: CompiledLine) = CompiledSet(lineStack.push(line))