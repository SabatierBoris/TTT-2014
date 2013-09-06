TARGET_NAME=commons.jar
SPACE=""

CC=nxjc
JAR=jar

SRC_DIR=src
TARGET_DIR=target

SRC=$(shell find $(SRC_DIR)/ -type f -name '*.java')
CLASS=$(patsubst $(SRC_DIR)/%.java, $(TARGET_DIR)/%.class, $(SRC))

CFLAGS=-d $(TARGET_DIR)
JARFLAGS=-cf

TARGET=$(TARGET_DIR)/$(TARGET_NAME)

all: $(TARGET)

$(TARGET): $(TARGET_DIR) $(CLASS)

%.jar:
	@echo "$(SPACE)Building `basename $@`"
	@$(JAR) -cf $@ $^

$(TARGET_DIR)/%.class: $(SRC_DIR)/%.java
	@echo "$(SPACE)Compiling `basename $^`" 
	@$(CC) $(CFLAGS) $^

$(TARGET_DIR):
	@echo "$(SPACE)Creating $@ directory" 
	@mkdir $(TARGET_DIR)

clean:
	@echo "$(SPACE)Removing class files" 
	@rm -f $(CLASS)
	@echo "$(SPACE)Removing `basename $(TARGET)`" 
	@rm -f $(TARGET)
	@echo "$(SPACE)Removing $(TARGET_DIR) directory" 
	@rm -rf $(TARGET_DIR)
