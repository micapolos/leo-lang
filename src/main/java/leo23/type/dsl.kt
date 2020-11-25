package leo23.type

val nilType: Type get() = NilType
val booleanType: Type get() = BooleanType
val textType: Type get() = TextType
val numberType: Type get() = NumberType
infix fun Type.pairTo(rhs: Type): Type = PairType(this, rhs)
val Type.vector: Type get() = VectorType(this)
fun params(vararg params: Type): List<Type> = listOf(*params)
infix fun List<Type>.arrowTo(type: Type): Type = ArrowType(this, type)
val Type.orNil: Type get() = OrNilType(this)
