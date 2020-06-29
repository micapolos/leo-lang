package leo16.lambda.type

data class TypeThunk(val scope: TypeScope, val type: Type)

infix fun TypeScope.thunk(type: Type) = TypeThunk(this, type)