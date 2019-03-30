package leo32

import leo.base.fold
import leo.base.ifNotNull

data class Type(
	val previous: Type?,
	val choice: Choice)

data class Choice(
	val lhs: Choice?,
	val field: Field)

data class Field(
	val key: String,
	val value: Type?)

fun type(choice: Choice, vararg choices: Choice): Type =
	Type(null, choice).fold(choices) { Type(this, it) }

fun type(field: Field, vararg fields: Field): Type =
	type(choice(field, *fields))

fun choice(field: Field, vararg fields: Field): Choice =
	Choice(null, field).fold(fields) { Choice(this, it) }

fun field(key: String, value: Type? = null): Field =
	Field(key, value)

fun ScriptWriter.write(type: Type): ScriptWriter =
	if (type.previous == null) write(type.choice)
	else writeEither(type)

fun ScriptWriter.write(choice: Choice): ScriptWriter =
	this
		.ifNotNull(choice.lhs, ScriptWriter::write)
		.write(choice.field)

fun ScriptWriter.write(field: Field): ScriptWriter =
	if (keywords.contains(field.key)) writeField("quote") { writeRaw(field) }
	else writeRaw(field)

fun ScriptWriter.writeRaw(field: Field): ScriptWriter =
	writeField(field.key) {
		ifNotNull(field.value, ScriptWriter::write)
	}

fun ScriptWriter.writeEither(type: Type): ScriptWriter =
	this
		.ifNotNull(type.previous, ScriptWriter::writeEither)
		.writeField("either") {
			write(type.choice)
		}

@Suppress("ReplaceSingleLineLet")
val Type.script
	get() =
		let { type ->
			script { write(type) }
		}

val keywords = setOf("quote", "either")