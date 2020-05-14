package leo16.library.reflection

import leo16.emptyDictionary
import leo16.emptyEvaluated
import leo16.native.reflectionDictionary
import leo16.scopeWithPublic

val native = emptyDictionary.scopeWithPublic(reflectionDictionary).emptyEvaluated