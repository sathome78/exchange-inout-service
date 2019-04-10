package generator;

import com.exrates.inout.service.ethereum.ExConvert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.utils.Files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

@RunWith(SpringRunner.class)
public class EthereumTokenCoinsGenerator {

    @Test
    @Ignore
    public void test() throws IOException {
        File input = new File("src/test/resources/input.txt");
        File output = new File("src/test/resources/output.txt");
        output.delete();
        output.createNewFile();

        String tokensText = Files.readString(input);
        String[] split = tokensText.split("@");
        for (int i = 0; i < split.length; i++) {
            if (!split[i].contains("Bean")) continue;
            split[i] = "@" + split[i];
            proccess(output, split[i]);
        }


    }

    private void proccess(File output, String tokensText) throws IOException {
        String s1 = "tokensList.add(\"";
        String contract = tokensText.substring(tokensText.indexOf(s1) + s1.length(), tokensText.indexOf("\");"));

        String s2 = "@Bean(name = ";
        String beanName = tokensText.substring(tokensText.indexOf(s2) + s2.length(), tokensText.indexOf(")")).replace("\"", "").trim();


        String s3 = "tokensList,";
        String params = tokensText.substring(tokensText.indexOf(s3) + s3.length());
        String[] split = params.replace("\n", "").trim().split(",");
        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].trim().replace("\"", "").replace(");", "").replace("}", "").trim();
        }
        Arrays.stream(split).forEach(System.out::println);

        String merchantName = split[0];
        String currencyName = split[1];
        boolean isERC  = Boolean.valueOf(split[3]);
        ExConvert.Unit u = Enum.valueOf(ExConvert.Unit.class, split[3].replace("ExConvert.Unit.", ""));

        StringBuilder builder = new StringBuilder();
        builder.append("    private EthereumTokenProperty " + currencyName.toUpperCase()).append(";\n");

        FileWriter writer = new FileWriter(output, true);
        writer.write(builder.toString());
        writer.flush();
        writer.close();
        System.out.println("d");
    }


}
