package leo14.untyped.dsl2.library

import leo14.untyped.dsl2.*

val html = library_ {
	text
	plus { attribute { name { text } } }
	does {
		given.text
		plus { given.plus.attribute.name.text }
	}

	assert {
		text("<")
		plus { attribute { name { text("href") } } }
		gives { text("<href") }
	}

	text
	plus { attribute { value { text } } }
	does {
		given.text
		plus { text("\"") }
		plus { given.plus.attribute.value.text }
		plus { text("\"") }
	}

	assert {
		text("<href=")
		plus { attribute { value { text("http://www.google.com") } } }
		gives { text("<href=\"http://www.google.com\"") }
	}

	text
	plus {
		attribute {
			name { text }
			value { text }
		}
	}
	does {
		given.text
		plus { attribute { given.plus.attribute.name } }
		plus { text("=") }
		plus { attribute { given.plus.attribute.value } }
	}

	assert {
		text("<")
		plus {
			attribute {
				name { text("href") }
				value { text("http://www.google.com") }
			}
		}
		gives { text("<href=\"http://www.google.com\"") }
	}

	text
	plus { tag { name { text } } }
	does {
		given.text
		plus { given.plus.tag.name.text }
	}

	assert {
		text("<")
		plus { tag { name { text("div") } } }
		gives { text("<div") }
	}
}

fun main() {
	html
}