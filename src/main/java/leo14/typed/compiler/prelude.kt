package leo14.typed.compiler

import leo14.Keyword
import leo14.Language
import leo14.leonardoScript
import leo14.stringIn
import leo14.typed.key
import leo14.typed.staticTyped
import leo14.typed.type
import leo14.typed.typed

fun <T> Language.preludeMemory(): Memory<T> =
	memory(
		nothingMemoryItem(),
		leonardoMemoryItem())

fun <T> Language.nothingMemoryItem(): MemoryItem<T> =
	item(
		key(type(Keyword.NOTHING stringIn this)),
		value(memoryBinding(typed(), isAction = false)))

fun <T> Language.leonardoMemoryItem(): MemoryItem<T> =
	item(
		key(type(Keyword.LEONARDO stringIn this)),
		value(memoryBinding(leonardoScript.staticTyped(), isAction = false)))