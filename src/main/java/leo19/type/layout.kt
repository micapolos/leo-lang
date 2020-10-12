package leo19.type

import leo.base.filter
import leo.base.foldMapFirstOrNull
import leo.base.indexed
import leo.base.notNullIf
import leo.base.runIf
import leo.base.size
import leo13.all
import leo13.filter
import leo13.linkOrNull
import leo13.seq

val Type.isStatic: Boolean
	get() =
		when (this) {
			is StructType -> struct.isStatic
			is ChoiceType -> false
			is ArrowType -> false
		}

val Struct.isStatic get() = fieldStack.all { isStatic }
val Field.isStatic get() = type.isStatic

val Struct.isComplex get() = fieldStack.filter { !isStatic }.linkOrNull?.stack?.linkOrNull != null
val Choice.isSimple get() = caseStack.all { type.isStatic }

val Type.isPossible: Boolean
	get() =
		when (this) {
			is StructType -> struct.isPossible
			is ChoiceType -> choice.isPossible
			is ArrowType -> true // really?
		}

val Struct.isPossible get() = fieldStack.all { type.isPossible }
val Field.isPossible get() = type.isPossible

val Choice.isPossible get() = possibleCaseCount != 0
val Case.isPossible get() = type.isPossible
val Choice.possibleCaseSeq get() = caseStack.seq.filter { isPossible }
val Choice.possibleCaseCount get() = possibleCaseSeq.size
fun Choice.possibleCaseIndex(name: String): Int =
	0.foldMapFirstOrNull(caseStack.seq) { case ->
		if (case.name == name) this to this
		else this.runIf(case.isPossible) { inc() } to null
	}!!

fun Struct.indexedFieldOrNull(name: String): IndexedValue<Field>? =
	0.foldMapFirstOrNull(fieldStack.seq) { field ->
		runIf(!field.isStatic) { inc() } to notNullIf(field.name == name) { indexed(field) }
	}

fun Choice.indexedCaseOrNull(name: String): IndexedValue<Case>? =
	0.foldMapFirstOrNull(caseStack.seq) { case ->
		runIf(case.isPossible) { inc() } to notNullIf(case.name == name) { indexed(case) }
	}

fun Type.indexedOrNull(name: String): IndexedValue<Type>? =
	structOrNull
		?.contentOrNull
		?.structOrNull
		?.indexedFieldOrNull(name)
		?.run { index indexed struct(value) }
