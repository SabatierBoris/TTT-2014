TARGET_NAME=wheeler.nxj
SPACE=""

JUNITJAR=/usr/share/java/junit.jar

CC=nxjc
LINK=nxjlink
JAR=jar

UPLOAD=nxjupload
UPLOADFLAGS=-r

SRC_DIR=src

TARGET_DIR=target

MAIN_CLASS=TTT/robots/Wheeler

LIBNXT_DIR=../libNXT
LIBNXT_TARGET=$(LIBNXT_DIR)/$(shell $(MAKE) -s -C $(LIBNXT_DIR) get_target)

COMMUNICATION_DIR=../communications/
COMMUNICATION_TARGET=$(COMMUNICATION_DIR)$(shell $(MAKE) -s -C $(COMMUNICATION_DIR) get_target)

FACTORY_DIR=../factory/
FACTORY_TARGET=$(FACTORY_DIR)$(shell $(MAKE) -s -C $(FACTORY_DIR) get_target)

SRC=$(shell find $(SRC_DIR)/ -type f -name '*.java')
CLASS=$(patsubst $(SRC_DIR)/%.java, $(TARGET_DIR)/%.class, $(SRC))

CFLAGS=-d $(TARGET_DIR) -cp $(SRC_DIR):$(LIBNXT_TARGET)
LINKFLAGS= -v -cp $(TARGET_DIR):$(LIBNXT_TARGET):$(COMMUNICATION_TARGET):$(FACTORY_TARGET)

TARGET=$(TARGET_NAME)

all: $(LIBNXT_TARGET) $(TARGET_DIR) $(TARGET)

$(LIBNXT_TARGET):
	@echo "$(SPACE)Building $(shell basename $@)"
	@$(MAKE) -s -C $(LIBNXT_DIR) SPACE="$(SPACE) - "

run: all
	@$(UPLOAD) $(UPLOADFLAGS) $(TARGET)

$(TARGET): $(CLASS) $(LIBNXT_TARGET)

%.nxj:
	@echo "$(SPACE)Building $(shell basename $@)"
	$(LINK) $(LINKFLAGS) -o $@ $(MAIN_CLASS)

$(TARGET_DIR)/%.class: $(SRC_DIR)/%.java
	@echo "$(SPACE)Compiling $(shell basename $^)" 
	@$(CC) $(CFLAGS) $<

$(TARGET_DIR):
	@echo "$(SPACE)Creating $@ directory" 
	@mkdir -p $@

get_target:
	@echo "$(TARGET)"

clean:
	@echo "$(SPACE)Removing class files" 
	@rm -f $(CLASS)
	@echo "$(SPACE)Removing $(shell basename $(TARGET))" 
	@rm -f $(TARGET)
	@echo "$(SPACE)Removing $(TARGET_DIR) directory" 
	@rm -rf $(TARGET_DIR)

.PHONY: clean get_target run $(LIBNXT_TARGET)
