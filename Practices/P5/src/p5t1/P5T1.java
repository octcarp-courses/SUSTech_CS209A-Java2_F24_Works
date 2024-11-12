package p5t1;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class P5T1 {
    public static void main(String[] args) {
        String inputFilePath = "p5t1/input.txt";
        String outputFilePath = "p5t1/output.txt";

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                P5T1.class.getClassLoader().getResourceAsStream(inputFilePath), StandardCharsets.UTF_8));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                     new FileOutputStream(P5T1.class.getClassLoader().getResource(outputFilePath).getFile()),
                     StandardCharsets.UTF_16))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String processedLine = processMapLine(line);
                writer.write(processedLine);
                writer.newLine();
            }

            System.out.println("Map processing completed successfully!");

        } catch (FileNotFoundException e) {
            System.out.println("The pathname does not exist.");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            System.out.println("The Character Encoding is not supported.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Failed or interrupted when doing the I/O operations");
            e.printStackTrace();
        }


    }

    private static String processMapLine(String line) {
        StringBuilder result = new StringBuilder();
        line.codePoints().forEach(codePoint -> {
            switch (codePoint) {
                case 0x007E:  // ('~')
                    result.append("\u2744");  //  ('❄')
                    break;
                case 0x1F332:  // ('🌲')
                    result.append("\u2B1C");  // ('⬜')
                    break;
                case 0x26F0:  // ('⛰')
                    result.appendCodePoint(codePoint);
                    break;
                default:
                    System.out.println("Unexpected code point: " + codePoint);
            }
        });

        return result.toString();
    }
}
