package leo16.lambda.type

import leo.base.fold
import leo14.untyped.leoString
import leo16.names.*

data class Type(val body: TypeBody, val isStatic: Boolean) {
	override fun toString() = reflect.leoString
}

sealed class TypeBody {
	override fun toString() = reflect.leoString
}

object EmptyTypeBody : TypeBody()

data class LinkTypeBody(val link: TypeLink) : TypeBody() {
	override fun toString() = super.toString()
}

data class FunctionTypeBody(val function: TypeFunction) : TypeBody() {
	override fun toString() = super.toString()
}

data class AlternativeTypeBody(val alternative: TypeAlternative) : TypeBody() {
	override fun toString() = super.toString()
}

data class NativeTypeBody(val native: Any) : TypeBody() {
	override fun toString() = super.toString()
}

data class LazyTypeBody(val lazy: TypeLazy) : TypeBody() {
	override fun toString() = super.toString()
}

data class RepeatingTypeBody(val repeating: TypeRepeating) : TypeBody() {
	override fun toString() = super.toString()
}

object RepeatTypeBody : TypeBody()

data class TypeLink(val previousType: Type, val lastSentence: TypeSentence)

data class TypeAlternative(val firstType: Type, val secondType: Type)

data class TypeSentence(val word: String, val type: Type) {
	override fun toString() = reflect.leoString
}

data class TypeFunction(val parameterType: Type, val resultType: Type) {
	override fun toString() = reflectScript.leoString
}

data class TypeLazy(val resultType: Type) {
	override fun toString() = reflectScript.leoString
}

data class TypeRepeating(val type: Type) {
	override fun toString() = reflectScript.leoString
}

val emptyTypeBody: TypeBody = EmptyTypeBody
val emptyType = emptyTypeBody.type
val TypeBody.type: Type get() = Type(this, isStatic)
val emptyTypeCase: TypeBody = EmptyTypeBody
val TypeLink.case: TypeBody get() = LinkTypeBody(this)
fun Type.linkTo(sentence: TypeSentence) = TypeLink(this, sentence)
fun String.sentenceTo(type: Type) = TypeSentence(this, type)
val Type.isEmpty get() = body.isEmpty
val TypeBody.isEmpty get() = (this is EmptyTypeBody)
val TypeBody.linkOrNull get() = (this as? LinkTypeBody)?.link
val TypeBody.alternativeOrNull get() = (this as? AlternativeTypeBody)?.alternative
val TypeBody.functionOrNull get() = (this as? FunctionTypeBody)?.function
val TypeBody.nativeOrNull get() = (this as? NativeTypeBody)?.native
fun Type.plus(sentence: TypeSentence) = linkTo(sentence).body.type
fun Type.alternative(type: Type) = TypeAlternative(this, type)
val TypeLink.body: TypeBody get() = LinkTypeBody(this)
val TypeAlternative.body: TypeBody get() = AlternativeTypeBody(this)
val TypeFunction.body: TypeBody get() = FunctionTypeBody(this)
val TypeLazy.body: TypeBody get() = LazyTypeBody(this)
infix fun Type.or(type: Type) = alternative(type).body.type
fun type(vararg sentences: TypeSentence) = emptyType.fold(sentences) { plus(it) }
fun String.sentence(vararg sentences: TypeSentence) = sentenceTo(type(*sentences))
operator fun String.invoke(vararg sentences: TypeSentence) = sentence(*sentences)
operator fun String.invoke(type: Type) = sentenceTo(type)
infix fun Type.functionGiving(type: Type) = TypeFunction(this, type)
infix fun Type.giving(type: Type) = functionGiving(type).body.type
val Any.nativeTypeBody: TypeBody get() = NativeTypeBody(this)
val Any.nativeType: Type get() = nativeTypeBody.type
val Type.lazy get() = TypeLazy(this)
val Type.repeatingBody get() = TypeRepeating(this)

val stringType: Type = String::class.java.nativeType
val intType: Type = Int::class.java.nativeType
val doubleType: Type = Double::class.java.nativeType
val booleanType: Type = type(_boolean(type(_false(type())).or(type(_true(type())))))