TARGET_NAME=factory.jar
SPACE=""

CC=javac
JAR=jar

SRC_DIR=src

TARGET_DIR=target

SRC=$(shell find $(SRC_DIR)/ -type f -name '*.java')
CLASS=$(patsubst $(SRC_DIR)/%.java, $(TARGET_DIR)/%.class, $(SRC))

CFLAGS=-d $(TARGET_DIR) -cp $(SRC_DIR) -Xlint
JARFLAGS=-cf

TARGET=$(TARGET_DIR)/$(TARGET_NAME)

all: $(TARGET_DIR) $(TARGET)

get_target:
	@echo "$(TARGET)"

$(TARGET): $(CLASS)

%.jar:
	@echo "$(SPACE)Building $(shell basename $@)"
	@$(JAR) -cf $@ $(patsubst $(TARGET_DIR)/%, -C $(TARGET_DIR) %, $^)

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
