TARGET_NAME=walker.nxj
SPACE=""

CC=nxjc
LINK=nxjlink
JAR=jar

SRC_DIR=src
TARGET_DIR=target

SRC=$(shell find $(SRC_DIR)/ -type f -name '*.java')
CLASS=$(patsubst $(SRC_DIR)/%.java, $(TARGET_DIR)/%.class, $(SRC))

CFLAGS=-d $(TARGET_DIR) -cp ../commons/target
LINKFLAGS=-cp $(TARGET_DIR):../commons/target

TARGET=$(TARGET_DIR)/$(TARGET_NAME)

all: $(TARGET_DIR) $(TARGET)

$(TARGET): $(CLASS)

%.nxj:
	@echo "$(SPACE)Building `basename $@`"
	@$(LINK) $(LINKFLAGS) -o $@ $(patsubst $(TARGET_DIR)/%.class, %, $<)

$(TARGET_DIR)/%.class: $(SRC_DIR)/%.java
	@echo "$(SPACE)Compiling `basename $^`" 
	@$(CC) $(CFLAGS) $<

$(TARGET_DIR):
	@echo "$(SPACE)Creating $@ directory" 
	@mkdir $(TARGET_DIR)

get_target:
	@echo "$(TARGET)"


clean:
	@echo "$(SPACE)Removing class files" 
	@rm -f $(CLASS)
	@echo "$(SPACE)Removing `basename $(TARGET)`" 
	@rm -f $(TARGET)
	@echo "$(SPACE)Removing $(TARGET_DIR) directory" 
	@rm -rf $(TARGET_DIR)

.PHONY: clean get_target
