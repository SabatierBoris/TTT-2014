TARGET_NAME=Monitor.jar
SPACE=""

COMMUNICATION_DIR=../communications/
COMMUNICATION_TARGET=$(COMMUNICATION_DIR)$(shell $(MAKE) -s -C $(COMMUNICATION_DIR) get_target)
COMMUNICATION_TARGET_DIR=$(shell dirname $(COMMUNICATION_TARGET))

CC=nxjpcc
JAR=jar
RUN=nxjpc

SRC_DIR=src

TARGET_DIR=target

SRC=$(shell find $(SRC_DIR)/ -type f -name '*.java')
CLASS=$(patsubst $(SRC_DIR)/%.java, $(TARGET_DIR)/%.class, $(SRC))

CFLAGS=-d $(TARGET_DIR) -cp $(SRC_DIR):$(COMMUNICATION_TARGET_DIR) -Xlint

TARGET=$(TARGET_DIR)/$(TARGET_NAME)

all: $(COMMUNICATION_TARGET) $(TARGET_DIR) $(TARGET)

$(COMMUNICATION_TARGET):
	@echo "$(SPACE)Creating $(shell basename $@)"
	@$(MAKE) -s -C $(COMMUNICATION_DIR) SPACE=" - "

run: all
	$(RUN) -cp $(TARGET_DIR)/$(TARGET_NAME):$(COMMUNICATION_TARGET) TTT.PC.Monitor
	#@$(RUN) -jar $(TARGET_DIR)/$(TARGET_NAME) TTT.PC.Monitor

get_target:
	@echo "$(TARGET)"

$(TARGET): $(CLASS)

%.jar:
	@echo "$(SPACE)Building $(shell basename $@)"
	@echo "Class-Path: $(COMMUNICATION_TARGET)" > Manifest.txt
	#@echo "Class-Path: $(shell basename $(COMMUNICATION_TARGET))" > Manifest.txt
	@$(JAR) -cfme $@ Manifest.txt TTT.PC.Monitor $(patsubst $(TARGET_DIR)/%,-C $(TARGET_DIR) %, $^)
	#@$(JAR) -cfme $@ Manifest.txt TTT.PC.Monitor $(patsubst $(TARGET_DIR)/%,-C $(TARGET_DIR) %, $^) -C $(COMMUNICATION_TARGET_DIR) $(shell basename $(COMMUNICATION_TARGET))
	@rm Manifest.txt

$(TARGET_DIR)/%.class: $(SRC_DIR)/%.java
	@echo "$(SPACE)compiling $(shell basename $^)" 
	@$(CC) $(CFLAGS) $^

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

.PHONY: clean get_target
