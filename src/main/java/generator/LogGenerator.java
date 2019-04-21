package generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class LogGenerator {
    private static String imports = "\nimport org.apache.logging.log4j.LogManager;\n" +
            "import org.apache.logging.log4j.Logger;\n";
    public static void main(String[] args) throws Throwable {
        File basedir = new File("/home/dudoser/IdeaProjects/exrates-inout-service/src/main/java/com/exrates/inout/");
        process(basedir);
    }

    private static void process(File basedir) throws Throwable {
        for (File file : basedir.listFiles()) {
            if(file.getName().equalsIgnoreCase("EDCServiceNodeImpl.java")){
                System.out.println("21");
            }
            if(file.isFile()){
                processFile(file);
            } else {
                process(file);
            }


        }
    }

    private static void processFile(File file) throws Throwable {
        String classText = readFile(file);
        if(classText.contains("public enum")) return;

        if (classText.contains("@Log4j")){

            String newClassText = getSubstring(classText);
            newClassText += "\n\n" +    "   private static final Logger log = LogManager.getLogger(tpcname);\n";
            String topicName = getTopicName(classText);
            if(topicName == null || topicName.length() == 0 ) throw new RuntimeException("Topic name = " + topicName);
            newClassText = newClassText.replace("tpcname", topicName);
            newClassText += classText.substring(classText.indexOf("{", classText.indexOf("public class")) + 1);

            newClassText = newClassText.replaceFirst(";", ";" + imports);
            write(newClassText, file);
        }
    }

    private static String getSubstring(String classText) {
        return classText.substring(0, classText.indexOf("{", classText.indexOf("public class")) + 1);
    }

    private static void write(String newClassText, File file) throws Throwable{
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, false));
        bufferedWriter.write(newClassText);
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    private static String getTopicName(String classText) {
        String topicInAnnotation = "@Log4j2(topic = ";
        if(classText.contains(topicInAnnotation)) {
            return extractTopicNameFromAnnotation(classText, topicInAnnotation);
        } else {
            return extractTopicNameAsClassName(classText);
        }
    }

    private static String extractTopicNameAsClassName(String classText) {
        try {
            String aClass = "public class";
            String from = classText.substring(classText.indexOf(aClass) + aClass.length() + 1);

            int endIndex = from.indexOf(" ");


            return from.substring(0 , endIndex).trim() + ".class";
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static String extractTopicNameFromAnnotation(String classText, String topicInAnnotation) {
        String temp = classText.substring(classText.indexOf(topicInAnnotation) + topicInAnnotation.length());
        return temp.substring(0, temp.indexOf(")"));
    }

    private static String readFile(File file) throws Exception{
        FileReader reader = new FileReader(file);
        StringBuilder builder = new StringBuilder();
        int c;
        do{
            c = reader.read();
            builder.append((char)c);
        } while (c != -1);
        return builder.toString();
    }
}
