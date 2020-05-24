package leo16.lambda

import leo13.StackLink
import leo13.onlyOrNull
import leo13.stackLink

data class Type(val choice: TypeChoice, val isStatic: Boolean) {
	override fun toString() = asField.toString()
}

data class TypeChoice(val caseStackLink: StackLink<TypeCase>) {
	override fun toString() = asField.toString()
}

sealed class TypeCase {
	override fun toString() = asField.toString()
}

object EmptyTypeCase : TypeCase()

data class LinkTypeCase(val link: TypeLink) : TypeCase() {
	override fun toString() = super.toString()
}

data class TypeLink(val type: Type, val field: TypeField)

sealed class TypeField {
	override fun toString() = asField.toString()
}

data class SentenceTypeField(val sentence: TypeSentence) : TypeField() {
	override fun toString() = super.toString()
}

data class FunctionTypeField(val function: TypeFunction) : TypeField() {
	override fun toString() = super.toString()
}

data class NativeTypeField(val native: Any) : TypeField() {
	override fun toString() = super.toString()
}

data class TypeSentence(val word: String, val type: Type) {
	override fun toString() = asField.toString()
}

data class TypeFunction(val input: Type, val output: Type) {
	override fun toString() = asField.toString()
}

val TypeChoice.type get() = Type(this, isStatic)
val TypeSentence.field: TypeField get() = SentenceTypeField(this)
val Any.nativeTypeField: TypeField get() = NativeTypeField(this)
val StackLink<TypeCase>.choice get() = TypeChoice(this)
val emptyTypeCase: TypeCase = EmptyTypeCase
val emptyType get() = emptyTypeCase.choice.type
val TypeLink.case: TypeCase get() = LinkTypeCase(this)
fun Type.linkTo(field: TypeField) = TypeLink(this, field)
fun String.sentenceTo(type: Type) = TypeSentence(this, type)
val TypeCase.choice get() = stackLink.choice
val TypeCase.type get() = choice.type
val TypeChoice.onlyCaseOrNull get() = caseStackLink.onlyOrNull
val TypeField.sentenceOrNull get() = (this as? SentenceTypeField)?.sentence
val TypeField.functionOrNull get() = (this as? FunctionTypeField)?.function
val TypeField.nativeOrNull get() = (this as? NativeTypeField)?.native
val Type.isEmpty get() = choice.onlyCaseOrNull?.isEmpty ?: false
val TypeCase.isEmpty get() = (this is EmptyTypeCase)
fun Type.plus(field: TypeField) = linkTo(field).type
