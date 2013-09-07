TARGET_NAME=walker.nxj
SPACE=""

JUNITJAR=/usr/share/java/junit.jar

CC=nxjc
LINK=nxjlink
JAR=jar

UPLOAD=nxjupload
UPLOADFLAGS=-r

BASE_SRC_DIR=src
MAIN_SRC_DIR=$(BASE_SRC_DIR)/main/java
TEST_SRC_DIR=$(BASE_SRC_DIR)/test/java

BASE_TARGET_DIR=target
MAIN_TARGET_DIR=$(BASE_TARGET_DIR)/main/java
TEST_TARGET_DIR=$(BASE_TARGET_DIR)/test/java

COMMONS_DIR=../commons
COMMONS_TARGET=$(COMMONS_DIR)/$(shell $(MAKE) -s -C $(COMMONS_DIR) get_target)
COMMONS_TARGET_DIR=$(shell dirname $(COMMONS_TARGET))

MAIN_SRC=$(shell find $(MAIN_SRC_DIR)/ -type f -name '*.java')
TEST_SRC=$(shell find $(TEST_SRC_DIR)/ -type f -name '*.java')
MAIN_CLASS=$(patsubst $(MAIN_SRC_DIR)/%.java, $(MAIN_TARGET_DIR)/%.class, $(MAIN_SRC))
TEST_CLASS=$(patsubst $(TEST_SRC_DIR)/%.java, $(TEST_TARGET_DIR)/%.class, $(TEST_SRC))

MAIN_CFLAGS=-d $(MAIN_TARGET_DIR) -cp $(COMMONS_TARGET_DIR)
TEST_CFLAGS=-d $(TEST_TARGET_DIR) -cp $(JUNITJAR):$(MAIN_TARGET_DIR)
LINKFLAGS=-cp $(MAIN_TARGET_DIR):$(COMMONS_TARGET_DIR)
TESTFLAGS=-cp $(JUNITJAR):$(TEST_TARGET_DIR):$(MAIN_TARGET_DIR)

TARGET=$(MAIN_TARGET_DIR)/$(TARGET_NAME)

all: $(MAIN_TARGET_DIR) $(TARGET)

run: all
	@$(UPLOAD) $(UPLOADFLAGS) $(TARGET)

test: all $(TEST_TARGET_DIR) runtest

runtest: $(TEST_CLASS) 
	@echo "$(SPACE)Starting the test"
	@java $(TESTFLAGS) org.junit.runner.JUnitCore $(subst /,.,$(patsubst $(TEST_TARGET_DIR)/%.class, %, $^))

$(TARGET): $(MAIN_CLASS)

%.nxj:
	@echo "$(SPACE)Building $(shell basename $@)"
	@$(LINK) $(LINKFLAGS) -o $@ $(patsubst $(MAIN_TARGET_DIR)/%.class, %, $<)

$(MAIN_TARGET_DIR)/%.class: $(MAIN_SRC_DIR)/%.java
	@echo "$(SPACE)Compiling $(shell basename $^)" 
	@$(CC) $(MAIN_CFLAGS) $<

$(TEST_TARGET_DIR)/%.class: $(TEST_SRC_DIR)/%.java
	@echo "$(SPACE)Compiling $(shell basename $^)" 
	@$(CC) $(TEST_CFLAGS) $<

$(MAIN_TARGET_DIR):
	@echo "$(SPACE)Creating $@ directory" 
	@mkdir -p $@

$(TEST_TARGET_DIR):
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

.PHONY: clean get_target run
