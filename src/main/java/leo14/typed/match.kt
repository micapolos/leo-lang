package leo14.typed

import leo13.Stack
import leo14.lambda.Term

data class Match<T>(val term: Term<T>, val optionStack: Stack<Option>, val inferredTypeOrNull: Type?)
