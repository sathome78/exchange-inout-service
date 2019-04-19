package generator;

import java.io.File;
import java.io.FileReader;

public class LogGenerator {
    public static void main(String[] args) throws Exception {
        File basedir = new File("/home/dudoser/IdeaProjects/exrates-inout-service");
        for (File file : basedir.listFiles()) {

            if(file.isFile()){
                String classText = readFile(file);
                if (classText.contains("@Log2j")){
                    String newClassText =
                }
            }

        }
    }

    private static String readFile(File file) throws Exception{
        FileReader reader = new FileReader(file);
        StringBuilder builder = new StringBuilder():
        int c;
        do{
            c = reader.read();
            builder.append((char)c);
        } while (c != -1);
        return builder.toString();
    }
}
