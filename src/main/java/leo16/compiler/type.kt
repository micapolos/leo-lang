package leo16.compiler

import leo13.StackLink
import leo13.stackLink

data class Type(val choice: TypeChoice, val alignment: Alignment, val size: Int) {
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

data class TypeSentence(val word: String, val type: Type) {
	override fun toString() = asField.toString()
}

data class TypeFunction(val input: Type, val output: Type, val index: Int) {
	override fun toString() = asField.toString()
}

val TypeChoice.type get() = Type(this, alignment, 0)
val TypeSentence.field: TypeField get() = SentenceTypeField(this)
val StackLink<TypeCase>.choice get() = TypeChoice(this)
val emptyTypeCase: TypeCase = EmptyTypeCase
val emptyType get() = Type(emptyTypeCase.choice, Alignment.ALIGNMENT_1, 0)
val TypeLink.case: TypeCase get() = LinkTypeCase(this)
fun Type.linkTo(field: TypeField) = TypeLink(this, field)
fun String.sentenceTo(type: Type) = TypeSentence(this, type)
val TypeCase.choice get() = stackLink.choice
val TypeCase.type get() = choice.type
