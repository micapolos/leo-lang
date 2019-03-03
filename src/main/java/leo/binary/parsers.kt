package leo.binary

import leo.characterOrNull
import leo.letterOrNull

val parser = Unit.parser

val bitParser = parser.bind { it.parser }
val booleanParser = bitParser.map { it.boolean }

val int0Parser = parser.map { int0 }
val int1Parser = int0Parser.bind { push(it).parser }
val int2Parser = int1Parser.bind { push(it).parser }
val int3Parser = int2Parser.bind { push(it).parser }
val int4Parser = int3Parser.bind { push(it).parser }
val int5Parser = int4Parser.bind { push(it).parser }

val letterOrNullParser = int5Parser.map { it.letterOrNull }
val characterOrNullParser = int5Parser.map { it.characterOrNull }