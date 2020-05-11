package leo16.native

import leo15.booleanName
import leo15.byteName
import leo15.charName
import leo15.doubleName
import leo15.floatName
import leo15.intName
import leo15.longName
import leo15.shortName
import leo16.emptyDictionary
import leo16.plus

val reflectionDictionary =
	emptyDictionary
		.plus(booleanName.definition(Boolean::class.java))
		.plus(charName.definition(Char::class.java))
		.plus(byteName.definition(Byte::class.java))
		.plus(shortName.definition(Short::class.java))
		.plus(intName.definition(Int::class.java))
		.plus(longName.definition(Long::class.java))
		.plus(floatName.definition(Float::class.java))
		.plus(doubleName.definition(Double::class.java))
		.plus(nameClassDefinition)
		.plus(classFieldDefinition)
		.plus(fieldGetDefinition)
		.plus(nativeGetFieldDefinition)
		.plus(classConstructorDefinition)
		.plus(constructorInvokeDefinition)
		.plus(classMethodDefinition)
		.plus(methodInvokeDefinition)
		.plus(nativeInvokeMethodDefinition)
		.plus(arrayStackDefinition)
		.plus(nullNativeDefinition)
		.plus(nativeBooleanDefinition)
		.plus(nativeObjectClassDefinition)

val printingDictionary =
	emptyDictionary
		.plus(printingDefinition)
