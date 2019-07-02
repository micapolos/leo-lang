package leo7

import leo.base.Empty
import leo32.Dict
import leo32.dict

data class Function(val tokenMatchDict: Dict<Token, Match>)

val identityFunction get() = Function(Empty.dict { bitSeq })

fun Function.apply(script: Script): Script = TODO()