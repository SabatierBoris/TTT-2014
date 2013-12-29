TARGET_NAME=walker.nxj
SPACE=""

JUNITJAR=/usr/share/java/junit.jar

CC=nxjc
LINK=nxjlink
JAR=jar

UPLOAD=nxjupload
UPLOADFLAGS=-r

SRC_DIR=src

TARGET_DIR=target

MAIN_CLASS=TTT/robots/Walker

COMMONS_DIR=../commons
COMMONS_TARGET=$(COMMONS_DIR)/$(shell $(MAKE) -s -C $(COMMONS_DIR) get_target)
COMMONS_TARGET_DIR=$(shell dirname $(COMMONS_TARGET))

SRC=$(shell find $(SRC_DIR)/ -type f -name '*.java')
CLASS=$(patsubst $(SRC_DIR)/%.java, $(TARGET_DIR)/%.class, $(SRC))

CFLAGS=-d $(TARGET_DIR) -cp $(TARGET_DIR):$(COMMONS_TARGET_DIR)
LINKFLAGS=-cp $(TARGET_DIR):$(COMMONS_TARGET_DIR)

TARGET=$(TARGET_DIR)/$(TARGET_NAME)

all: $(COMMONS_TARGET) $(TARGET_DIR) $(TARGET)

$(COMMONS_TARGET):
	@echo "$(SPACE)Building $(shell basename $(COMMONS_TARGET))"
	@$(MAKE) -s -C $(COMMONS_DIR) SPACE=" - "

run: all
	@$(UPLOAD) $(UPLOADFLAGS) $(TARGET)

$(TARGET): $(CLASS)

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
	@echo "$(SPACE)Removing $(BASE_TARGET_DIR) directory" 
	@rm -rf $(BASE_TARGET_DIR)

.PHONY: clean get_target run $(COMMONS_TARGET)
