package leo16.native

import leo.base.println
import leo13.array
import leo13.map
import leo13.stack
import leo14.untyped.typed.loadClass
import leo16.accessOrNull
import leo16.does
import leo16.getOrNull
import leo16.invoke
import leo16.linkOrNull
import leo16.names.*
import leo16.nativeValue
import leo16.onlyValue
import leo16.printed
import leo16.sentence
import leo16.stackOrNull
import leo16.theNativeOrNull
import leo16.thingOrNull
import leo16.value
import leo16.valueValue
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method

fun String.definition(class_: Class<*>) =
	value(_class(this())).does {
		_class(class_.nativeValue).onlyValue
	}

val nativeObjectClassDefinition =
	value(_class(_object(_any(_native())))).does {
		val class_ = this
			.getOrNull(_class)!!
			.getOrNull(_object)!!
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value!!
			.javaClass
		value(_class(class_.nativeValue))
	}

val nullNativeDefinition =
	value(_native(_null())).does {
		null.nativeValue
	}

val nativeBooleanDefinition =
	value(_boolean(_any(_native()))).does {
		this
			.getOrNull(_boolean)!!
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.run { value as Boolean }
			.sentence
			.onlyValue
	}

val nameClassDefinition =
	value(_class(_name(_text(_any(_native()))))).does {
		val name = this
			.getOrNull(_class)!!
			.getOrNull(_name)!!
			.getOrNull(_text)!!
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value as String
		_class(name.loadClass.nativeValue).onlyValue
	}

val classFieldDefinition =
	value(
		_class(_any(_native())),
		_field(_name(_text(_any(_native()))))
	).does {
		val class_ = this
			.getOrNull(_class)!!
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value as Class<*>
		val name = this
			.getOrNull(_field)!!
			.getOrNull(_name)!!
			.getOrNull(_text)!!
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value as String
		_field(class_.getField(name).nativeValue).onlyValue
	}

val fieldGetDefinition =
	value(_get(_field(_any(_native())))).does {
		val field = this
			.getOrNull(_get)!!
			.getOrNull(_field)!!
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value as Field
		field.get(null).nativeValue
	}

val nativeGetFieldDefinition =
	value(
		_any(_native()),
		_get(_field(_any(_native())))
	).does {
		val object_ = this
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value
		val field = this
			.getOrNull(_get)!!
			.getOrNull(_field)!!
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value as Field
		field.get(object_).nativeValue
	}

val classConstructorDefinition =
	value(
		_class(_any(_native())),
		_constructor(_parameter(_list(_anything())))
	).does {
		val class_ = this
			.getOrNull(_class)!!
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value as Class<*>
		val classes = this
			.getOrNull(_constructor)!!
			.getOrNull(_parameter)!!
			.getOrNull(_list)!!
			.stackOrNull!!
			.map {
				this
					.accessOrNull(_class)!!
					.getOrNull(_native)!!
					.theNativeOrNull!!
					.value as Class<*>
			}
			.array
		_constructor(class_.getConstructor(*classes).nativeValue).onlyValue
	}

val constructorInvokeDefinition =
	value(
		_constructor(_any(_native())),
		_invoke(_parameter(_list(_anything())))
	).does {
		val constructor = this
			.getOrNull(_constructor)!!
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value as Constructor<*>
		val args = this
			.getOrNull(_invoke)!!
			.getOrNull(_parameter)!!
			.getOrNull(_list)!!
			.stackOrNull!!
			.map { theNativeOrNull!!.value }
			.array
		constructor.newInstance(*args).nativeValue
	}

val classMethodDefinition =
	value(
		_class(_any(_native())),
		_method(
			_name(_text(_any(_native()))),
			_parameter(_list(_anything()))
		)
	).does {
		val class_ = this
			.getOrNull(_class)!!
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value as Class<*>
		val name = this
			.getOrNull(_method)!!
			.getOrNull(_name)!!
			.getOrNull(_text)!!
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value as String
		val classes = this
			.getOrNull(_method)!!
			.getOrNull(_parameter)!!
			.getOrNull(_list)!!
			.stackOrNull!!
			.map {
				this
					.accessOrNull(_class)!!
					.getOrNull(_native)!!
					.theNativeOrNull!!
					.value as Class<*>
			}
			.array
		_method(class_.getMethod(name, *classes).nativeValue).onlyValue
	}

val methodInvokeDefinition =
	value(
		_method(_any(_native())),
		_invoke(_parameter(_list(_anything())))
	).does {
		val method = this
			.getOrNull(_method)!!
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value as Method
		val args = this
			.getOrNull(_invoke)!!
			.getOrNull(_parameter)!!
			.getOrNull(_list)!!
			.stackOrNull!!
			.map { theNativeOrNull!!.value }
			.array
		method.invoke(null, *args).nativeValue
	}

val nativeInvokeMethodDefinition =
	value(
		_any(_native()),
		_invoke(
			_method(_any(_native())),
			_parameter(_list(_anything())))
	).does {
		val object_ = this
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value
		val method = this
			.getOrNull(_invoke)!!
			.getOrNull(_method)!!
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value as Method
		val args = this
			.getOrNull(_invoke)!!
			.getOrNull(_parameter)!!
			.getOrNull(_list)!!
			.stackOrNull!!
			.map { theNativeOrNull!!.value }
			.array
		method.invoke(object_, *args).nativeValue
	}

val arrayStackDefinition =
	value(_list(_array(_any(_native())))).does {
		val array = this
			.getOrNull(_list)!!
			.getOrNull(_array)!!
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value as Array<*>
		stack(*array).map { nativeValue }.valueValue
	}

val printingDefinition =
	value(_printing(_anything())).does {
		val thing = this
			.getOrNull(_printing)!!
			.thingOrNull!!
		thing.also { thing.printed.println }
	}

val leoHeadDefinition =
	value(_head(_anything())).does {
		this
			.getOrNull(_head)!!
			.thingOrNull!!
			.linkOrNull
			?.lastSentence
			?.rhsValue
			?: this.getOrNull(_head)!!
	}

val leoTailDefinition =
	value(_tail(_anything())).does {
		this
			.getOrNull(_tail)!!
			.thingOrNull!!
			.linkOrNull
			?.previousValue
			?: this.getOrNull(_tail)!!
	}

val leoOpDefinition =
	value(_op(_anything())).does {
		this
			.getOrNull(_op)!!
			.thingOrNull!!
			.linkOrNull
			?.lastSentence
			?.word
			?.invoke()
			?.rhsValue
			?: this.getOrNull(_op)!!
	}
