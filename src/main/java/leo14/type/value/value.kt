package leo14.type.value

import leo14.lambda.Term
import leo14.type.*

data class Value<V>(val term: Term<V>, val thunk: TypeThunk)

data class NativeValue<V>(val term: Term<V>, val thunk: NativeThunk)
data class StructureValue<V>(val term: Term<V>, val thunk: StructureThunk)
data class ListValue<V>(val term: Term<V>, val thunk: ListThunk)
data class ChoiceValue<V>(val term: Term<V>, val thunk: ChoiceThunk)
data class ActionValue<V>(val term: Term<V>, val thunk: ActionThunk)
data class RecursiveValue<V>(val term: Term<V>, val thunk: RecursiveThunk)
data class FieldValue<V>(val term: Term<V>, val thunk: FieldThunk)
