SHELL := /bin/bash

test:
	@if ! [ -z "${class}" ] && ! [ -z "${method}" ]; then\
		mvn -Dtest="${class}#${method}" test;\
	else\
		mvn test;\
	fi

package:
	mvn package;

run:
	mvn spring-boot:run;
