TARGET_NAME=libNXT.jar
SPACE=""

CC=nxjc
JAR=jar

SRC_DIR=src

TARGET_DIR=target

SRC=$(shell find $(SRC_DIR)/ -type f -name '*.java')
CLASS=$(patsubst $(SRC_DIR)/%.java, $(TARGET_DIR)/%.class, $(SRC))

COMMUNICATION_DIR=../communications/
COMMUNICATION_TARGET=$(COMMUNICATION_DIR)$(shell $(MAKE) -s -C $(COMMUNICATION_DIR) get_target)

CFLAGS=-d $(TARGET_DIR) -cp $(SRC_DIR):$(COMMUNICATION_TARGET) -Xlint
JARFLAGS=-cf

TARGET=$(TARGET_NAME)

all: $(COMMUNICATION_TARGET) $(TARGET_DIR) $(TARGET)

get_target:
	@echo "$(TARGET)"

$(TARGET): $(CLASS)

%.jar:
	@echo "$(SPACE)Building $(shell basename $@)"
	@echo "Class-Path: $(COMMUNICATION_TARGET)" > Manifest.txt
	@$(JAR) -cfm $@ Manifest.txt $(patsubst $(TARGET_DIR)/%, -C $(TARGET_DIR) %, $^)
	@rm Manifest.txt

$(TARGET_DIR)/%.class: $(SRC_DIR)/%.java
	@echo "$(SPACE)compiling $(shell basename $^)" 
	@$(CC) $(CFLAGS) $^

$(TARGET_DIR):
	@echo "$(SPACE)Creating $@ directory" 
	@mkdir -p $@

$(COMMUNICATION_TARGET):
	@echo "$(SPACE)Creating $(shell basename $@)"
	@$(MAKE) -s -C $(COMMUNICATION_DIR) SPACE="$(SPACE) - "

clean:
	@echo "$(SPACE)Removing class files" 
	@rm -f $(CLASS)
	@echo "$(SPACE)Removing $(shell basename $(TARGET))" 
	@rm -f $(TARGET)
	@echo "$(SPACE)Removing $(TARGET_DIR) directory" 
	@rm -rf $(TARGET_DIR)

.PHONY: clean get_target $(COMMUNICATION_TARGET)
