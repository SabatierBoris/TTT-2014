package TTT.commons.factory;

import java.util.Set;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.StringTokenizer;

import java.io.PrintWriter;
import java.io.IOException;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import javax.tools.StandardLocation;
import javax.tools.Diagnostic.Kind;

import TTT.commons.factory.ItemFactory;

@SupportedAnnotationTypes(value = { "TTT.commons.factory.ItemFactory" })
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class FactoryProcessor extends AbstractProcessor {
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv){
		Messager messager = processingEnv.getMessager();
		Filer filer = processingEnv.getFiler();
		for(TypeElement te : annotations){
			Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(te);
			if(!elements.isEmpty()){
				Hashtable<String,ArrayList<Element>> map = new Hashtable<String,ArrayList<Element>>();
				for(Element element : elements){
					messager.printMessage(Kind.NOTE,element.getSimpleName());
					ItemFactory i = element.getAnnotation(ItemFactory.class);
					if(i != null){
						String val = i.factoryName();
						if(!map.containsKey(val)){
							map.put(val, new ArrayList<Element>());
						}
						map.get(val).add(element);
					}
				}
				for(String key : map.keySet()){
					try{
						String packageName = "";
						String className = "";
						StringTokenizer st = new StringTokenizer(key,".");
						boolean first;
						while(st.hasMoreTokens()){
							String buff = st.nextToken();
							if(st.hasMoreTokens()){
								if(!packageName.equals("")){
									packageName += ".";
								}
								packageName += buff;
							} else {
								className = buff;
							}
						}
						messager.printMessage(Kind.NOTE," package : " + packageName);
						messager.printMessage(Kind.NOTE," class : " + className);

						PrintWriter factopw = new PrintWriter(filer.createResource(StandardLocation.SOURCE_OUTPUT, "" , packageName.replace(".","/") + "/" + className + "Factory.java").openOutputStream());

						this.addPackageName(factopw,packageName);
						this.addGeneratedComment(factopw);

						factopw.println("public class " + className + "Factory extends Abstract" + className + "Factory {");

						factopw.println("\tpublic " + className + "Factory(){");
						factopw.println("\t\tsuper();");
						factopw.println("\t}");

						factopw.println("\tpublic Object getObject(int id){");

						factopw.println("\t\tObject ret = null;");
						factopw.println("\t\tswitch(id){");

						for(Element element : map.get(key)){
							factopw.println("\t\t\tcase " + element.getSimpleName() + ".ID:");
							factopw.println("\t\t\t\tret = new " + element.getSimpleName() + "();");
							factopw.println("\t\t\t\tbreak;");
						}
						factopw.println("\t\t}");

						factopw.println("\t\treturn ret;");
						factopw.println("\t}");
						factopw.println("}");
						factopw.close();
					} catch(IOException ioe){
						messager.printMessage(Kind.ERROR, ioe.getMessage());
					}
				}
			}
		}
		return true;
	}

	private void addPackageName(PrintWriter pw, String packageName){
		pw.println("package " + packageName + ";");
		pw.println("");
	}

	private void addImport(PrintWriter pw, String importName){
		pw.println("import " + importName + ";");
		pw.println("");
	}

	private void addGeneratedComment(PrintWriter pw){
		pw.println("/**");
		pw.println(" * This is a generated file");
		pw.println(" */");
	}
}
