TARGET_NAME=communication.jar
SPACE=""

FACTORY_DIR=../factory/
FACTORY_TARGET=$(FACTORY_DIR)$(shell $(MAKE) -s -C $(FACTORY_DIR) get_target)
FACTORY_TARGET_DIR=$(shell dirname $(FACTORY_TARGET))

PROCESSOR=TTT.commons.factory.FactoryProcessor

CC=nxjc
JAR=jar

SRC_DIR=src

TARGET_DIR=target

SRC=$(shell find $(SRC_DIR)/ -type f -name '*.java')
CLASS=$(patsubst $(SRC_DIR)/%.java, $(TARGET_DIR)/%.class, $(SRC))

CFLAGS=-d $(TARGET_DIR) -cp $(SRC_DIR):$(FACTORY_TARGET_DIR) -Xlint
JARFLAGS=-cf

TARGET=$(TARGET_DIR)/$(TARGET_NAME)

all: $(TARGET_DIR) processor Remote$(TARGET)

processor: $(FACTORY_TARGET)
	@echo "Processor"
	@javac $(CFLAGS) -s $(SRC_DIR) -processor $(PROCESSOR) $(SRC)

get_target:
	@echo "$(TARGET)"

Remote$(TARGET):
	@$(MAKE) -s clean
	@$(MAKE) -s $(TARGET_DIR)
	@$(MAKE) -s $(TARGET)


$(TARGET): $(CLASS)

%.jar:
	@echo "$(SPACE)Building $(shell basename $@)"
	@echo "Class-Path: $(FACTORY_TARGET)" > Manifest.txt
	#@echo "Class-Path: $(shell basename $(FACTORY_TARGET))" > Manifest.txt
	@$(JAR) -cfm $@ Manifest.txt $(patsubst $(TARGET_DIR)/%, -C $(TARGET_DIR) %, $^)
	#@$(JAR) -cfm $@ Manifest.txt $(patsubst $(TARGET_DIR)/%, -C $(TARGET_DIR) %, $^) -C $(FACTORY_TARGET_DIR) $(shell basename $(FACTORY_TARGET))
	@rm Manifest.txt

$(FACTORY_TARGET):
	@echo "$(SPACE)Creating $(shell basename $@)" 
	@$(MAKE) -s -C $(FACTORY_DIR) SPACE=" - "

$(TARGET_DIR)/%.class: $(SRC_DIR)/%.java
	@echo "$(SPACE)compiling $(shell basename $^)" 
	$(CC) $(CFLAGS) $^

$(TARGET_DIR):
	@echo "$(SPACE)Creating $@ directory" 
	@mkdir -p $@

clean:
	@echo "$(SPACE)Removing class files" 
	@rm -f $(CLASS)
	@echo "$(SPACE)Removing $(shell basename $(TARGET))" 
	@rm -f $(TARGET)
	@echo "$(SPACE)Removing $(TARGET_DIR) directory" 
	@rm -rf $(TARGET_DIR)

.PHONY: clean get_target $(FACTORY_TARGET)
