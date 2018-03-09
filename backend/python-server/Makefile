#
#  Standard Makefile supports targets build, install and clean + static
#  code checking. make install respects DESTDIR and PREFIX.


ifeq ($(DESTDIR),)
    DESTDIR     = $(CURDIR)/install
endif
ifeq ($(PREFIX),)
    PREFIX      = /usr/local
endif
UNITDIR         = $(shell pkg-config systemd --variable=systemdsystemunitdir)

# vim-compatible error reporting:
pylint_template = {path}:{line}: [{msg_id}({symbol}), {obj}] {msg}


all:	build

build:
	@echo "Nothing to do"	

install: .phony
	-mkdir -p $(DESTDIR)$(UNITDIR)
	cp -a rfidserver.service $(DESTDIR)$(UNITDIR)
	-mkdir -p $(DESTDIR)$(PREFIX)/bin
	cp -a rfidserver $(DESTDIR)$(PREFIX)/bin

clean: .phony
	@echo "Nothing to do"	
	

pylint: rfidserver
	pylint3  --msg-template='$(pylint_template)' $?

pydocstyle: rfidserver
	pydocstyle $?

pycodestyle: rfidserver
	pycodestyle $?

.phony:
