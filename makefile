SPACE=""

UPLOAD=nxjupload
UPLOADFLAGS=-r

COMMONS_DIR=./commons

WALKER_DIR=./walker-2014


all: commons walker

test: all
	@echo "$(SPACE)Testing commons"
	@$(MAKE) -s -C $(COMMONS_DIR) SPACE=" - " $@
	@echo "$(SPACE)Testing walker"
	@$(MAKE) -s -C $(WALKER_DIR) SPACE=" - " $@

commons: 
	@echo "$(SPACE)Building $@"
	@$(MAKE) -s -C $(COMMONS_DIR) SPACE=" - "

walker: commons 
	@echo "$(SPACE)Building $@"
	@$(MAKE) -s -C $(WALKER_DIR) SPACE=" - "

runWalker: walker
	@$(MAKE) -s -C $(WALKER_DIR) SPACE=" - " run

clean: 
	@echo "$(SPACE)Cleaning commons"
	@$(MAKE) -s -C $(COMMONS_DIR) SPACE=" - " clean
	@echo "$(SPACE)Cleaning walker"
	@$(MAKE) -s -C $(WALKER_DIR) SPACE=" - " clean

.PHONY: commons walker runWalker
