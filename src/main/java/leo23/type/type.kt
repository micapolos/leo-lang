package leo23.type

sealed class Type
object NilType : Type()
object BooleanType : Type()
object TextType : Type()
object NumberType : Type()
data class PairType(val lhsType: Type, val rhsType: Type) : Type()
data class VectorType(val itemType: Type) : Type()
data class ArrowType(val paramTypes: List<Type>, val returnType: Type) : Type()
data class OrNilType(val itemType: Type) : Type()
