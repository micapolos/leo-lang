package leo15.lambda.runtime.type

import leo15.lambda.runtime.builder.Term

data class Value<V>(val term: Term<V>)