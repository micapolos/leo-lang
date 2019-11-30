package leo14.typed.compiler.js

import leo14.parser.newSpacedTokenParser
import leo14.tokenReader
import leo14.typed.compiler.CharCompiler

val emptyCharCompiler = CharCompiler(newSpacedTokenParser, emptyCompiler.tokenReader)
