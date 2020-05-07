package leo16.library

import leo15.dsl.*
import leo16.dictionary_

fun main() {
	url
}

val url = dictionary_ {
	reflection.import
	list.import

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
						list {
							item { "java.lang.String".text.name.class_ }
						}
					}
				}
			}

			url.get.protocol.method.is_ {
				url.class_.method {
					name { "getProtocol".text }
					parameter { empty.list }
				}
			}

			url.get.host.method.is_ {
				url.class_.method {
					name { "getHost".text }
					parameter { empty.list }
				}
			}

			url.get.port.method.is_ {
				url.class_.method {
					name { "getPort".text }
					parameter { empty.list }
				}
			}

			url.get.path.method.is_ {
				url.class_.method {
					name { "getPath".text }
					parameter { empty.list }
				}
			}

			url.get.query.method.is_ {
				url.class_.method {
					name { "getQuery".text }
					parameter { empty.list }
				}
			}

			url.get.method.is_ {
				url.kotlin.class_
				method {
					name { "get".text }
					parameter { list { item { url.class_ } } }
				}
			}
		}
	}

	any.text.url
	gives {
		string.url.constructor
		invoke { parameter { list { item { url.text.native } } } }
		url
	}

	any.native.url.read
	gives {
		url.get.method
		invoke { parameter { list { item { read.url.native } } } }
		text.read
	}

	any.native.url.protocol.gives {
		protocol.url.native.invoke {
			method { url.get.protocol }
			parameter { empty.list }
		}.text.protocol
	}

	any.native.url.host.gives {
		host.url.native.invoke {
			method { url.get.host }
			parameter { empty.list }
		}.text.host
	}

	any.native.url.path.gives {
		path.url.native.invoke {
			method { url.get.path }
			parameter { empty.list }
		}.text.path
	}

	test { "http://mwiacek.com".text.url.protocol gives { "http".text.protocol } }
	test { "http://mwiacek.com".text.url.host gives { "mwiacek.com".text.host } }
	test { "http://mwiacek.com".text.url.path gives { "".text.path } }
	test { "http://mwiacek.com/index.html".text.url.path gives { "/index.html".text.path } }

	any.native.url.reflect.gives {
		url {
			this_ { reflect.url.protocol }
			this_ { reflect.url.host }
			this_ { reflect.url.path }
		}
	}

	test {
		"http://mwiacek.com/index.html".text.url.reflect.gives {
			url {
				protocol { "http".text }
				host { "mwiacek.com".text }
				path { "/index.html".text }
			}
		}
	}

	any.native.url.browse
	gives {
		"java.awt.Desktop".text.name.class_
		method {
			name { "getDesktop".text }
			parameter { empty.list }
		}
		invoke { parameter { empty.list } }
		invoke {
			"java.awt.Desktop".text.name.class_
			method {
				name { "browse".text }
				parameter { list { item { "java.net.URI".text.name.class_ } } }
			}
			parameter {
				list {
					item {
						browse.url.native
						invoke {
							url.class_
							method {
								name { "toURI".text }
								parameter { empty.list }
							}
							parameter { empty.list }
						}
					}
				}
			}
		}
		clear
	}

	test { "http://mwiacek.com".text.url.browse.gives { nothing_ } }
}