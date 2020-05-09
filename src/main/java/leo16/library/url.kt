package leo16.library

import leo15.dsl.*
import leo16.dictionary_

fun main() {
	url
}

val url = dictionary_ {
	reflection.import
	stack.import
	number.import

	import {
		dictionary {
			url.kotlin.class_
			is_ { "leo.base.UrlKt".text.name.class_ }

			url.class_
			is_ { "java.net.URL".text.name.class_ }

			string.url.constructor
			is_ {
				url.class_
				constructor {
					parameter {
						stack {
							item { "java.lang.String".text.name.class_ }
						}
					}
				}
			}

			url.get.protocol.method.is_ {
				url.class_.method {
					name { "getProtocol".text }
					parameter { empty.stack }
				}
			}

			url.get.user.info.method.is_ {
				url.class_.method {
					name { "getUserInfo".text }
					parameter { empty.stack }
				}
			}

			url.get.host.method.is_ {
				url.class_.method {
					name { "getHost".text }
					parameter { empty.stack }
				}
			}

			url.get.port.method.is_ {
				url.class_.method {
					name { "getPort".text }
					parameter { empty.stack }
				}
			}

			url.get.path.method.is_ {
				url.class_.method {
					name { "getPath".text }
					parameter { empty.stack }
				}
			}

			url.get.query.method.is_ {
				url.class_.method {
					name { "getQuery".text }
					parameter { empty.stack }
				}
			}

			url.get.ref.method.is_ {
				url.class_.method {
					name { "getRef".text }
					parameter { empty.stack }
				}
			}

			url.get.method.is_ {
				url.kotlin.class_
				method {
					name { "get".text }
					parameter { stack { item { url.class_ } } }
				}
			}
		}
	}

	any.text.url
	gives {
		string.url.constructor
		invoke { parameter { stack { item { url.text.native } } } }
		url
	}

	any.native.url.read
	gives {
		url.get.method
		invoke { parameter { stack { item { read.url.native } } } }
		text.read
	}

	any.native.url.protocol.gives {
		protocol.url.native.invoke {
			method { url.get.protocol }
			parameter { empty.stack }
		}.text.protocol
	}

	any.native.url.user.gives {
		user.url.native
		invoke {
			method { url.get.user.info }
			parameter { empty.stack }
		}
		give {
			native.object_.equals_ { null_.native }.boolean
			match {
				true_ { none }
				false_ { native.text }
			}.user
		}
	}

	any.native.url.host.gives {
		host.url.native.invoke {
			method { url.get.host }
			parameter { empty.stack }
		}.text.host
	}

	any.native.url.port.gives {
		port.url.native.invoke {
			method { url.get.port }
			parameter { empty.stack }
		}.int.number
		give {
			number.equals_ { (-1).number }
			match {
				true_ { none }
				false_ { number }
			}.port
		}
	}

	any.native.url.path.gives {
		path.url.native.invoke {
			method { url.get.path }
			parameter { empty.stack }
		}.text.path
	}

	any.native.url.query.gives {
		query.url.native
		invoke {
			method { url.get.query }
			parameter { empty.stack }
		}
		give {
			native.object_.equals_ { null_.native }.boolean
			match {
				true_ { none }
				false_ { native.text }
			}.query
		}
	}

	any.native.url.fragment.gives {
		fragment.url.native
		invoke {
			method { url.get.ref }
			parameter { empty.stack }
		}
		give {
			native.object_.equals_ { null_.native }.boolean
			match {
				true_ { none }
				false_ { native.text }
			}.fragment
		}
	}

	test { "http://mwiacek.com".text.url.protocol gives { "http".text.protocol } }
	test { "http://mwiacek.com".text.url.host gives { "mwiacek.com".text.host } }
	test { "http://mwiacek.com".text.url.port gives { none.port } }
	test { "http://mwiacek.com:8080".text.url.port gives { 8080.number.port } }
	test { "http://mwiacek.com".text.url.path gives { "".text.path } }
	test { "http://mwiacek.com/index.html".text.url.path gives { "/index.html".text.path } }
	test { "http://mwiacek.com".text.url.query gives { none.query } }
	test { "http://mwiacek.com?q=foo".text.url.query gives { "q=foo".text.query } }
	test { "http://mwiacek.com".text.url.fragment gives { none.fragment } }
	test { "http://mwiacek.com#foo".text.url.fragment gives { "foo".text.fragment } }
	test { "http://mwiacek.com".text.url.user gives { none.user } }
	test { "http://foo@mwiacek.com".text.url.user gives { "foo".text.user } }

	any.native.url.reflect.gives {
		url {
			this_ { reflect.url.protocol }
			this_ { reflect.url.user }
			this_ { reflect.url.host }
			this_ { reflect.url.port }
			this_ { reflect.url.path }
			this_ { reflect.url.query }
			this_ { reflect.url.fragment }
		}
	}

	test {
		"http://mwiacek.com/index.html".text.url.reflect.gives {
			url {
				protocol { "http".text }
				user { none }
				host { "mwiacek.com".text }
				port { none }
				path { "/index.html".text }
				query { none }
				fragment { none }
			}
		}
	}

	any.native.url.browse
	gives {
		"java.awt.Desktop".text.name.class_
		method {
			name { "getDesktop".text }
			parameter { empty.stack }
		}
		invoke { parameter { empty.stack } }
		invoke {
			"java.awt.Desktop".text.name.class_
			method {
				name { "browse".text }
				parameter { stack { item { "java.net.URI".text.name.class_ } } }
			}
			parameter {
				stack {
					item {
						browse.url.native
						invoke {
							url.class_
							method {
								name { "toURI".text }
								parameter { empty.stack }
							}
							parameter { empty.stack }
						}
					}
				}
			}
		}
		give { nothing }
	}
}