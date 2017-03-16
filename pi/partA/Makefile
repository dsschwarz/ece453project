MK_DIR = mkdir
OUT = out

JCC = javac
J_FLAGS = -g -classpath src -d out
.SUFFIXES: .java .class

all: Main.class

Main.class: src/Main.java
	$(JCC) $(J_FLAGS) src/Main.java

classes: $(CLASSES:.java=.class)

clean:
	rm -rf out

$(shell   mkdir -p $(OUT))