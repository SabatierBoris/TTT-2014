SPACE=""

UPLOAD=nxjupload
UPLOADFLAGS=-r

COMMONS_DIR=./commons

WALKER_DIR=./walker-2014
WALKER_TARGET=$(shell $(MAKE) -s -C $(WALKER_DIR) get_target)


all: commons walker

commons: 
	@echo "$(SPACE)Building $@"
	@$(MAKE) -s -C $(COMMONS_DIR) SPACE=" - "

walker: commons 
	@echo "$(SPACE)Building $@"
	@$(MAKE) -s -C $(WALKER_DIR) SPACE=" - "

runWalker: walker
	@$(UPLOAD) $(UPLOADFLAGS) $(WALKER_TARGET)

clean: 
	@echo "$(SPACE)Cleaning commons"
	@$(MAKE) -s -C $(COMMONS_DIR) SPACE=" - " clean
	@echo "$(SPACE)Cleaning walker"
	@$(MAKE) -s -C $(WALKER_DIR) SPACE=" - " clean

.PHONY: commons walker runWalker
