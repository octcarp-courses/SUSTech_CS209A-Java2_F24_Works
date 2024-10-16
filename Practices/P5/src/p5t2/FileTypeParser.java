package p5t2;

import java.io.FileInputStream;
import java.io.IOException;

public class FileTypeParser {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Wrong format, please give single <filename> as argument.");
            return;
        }

        String filename = args[0];
        try {
            FileInputStream fis = new FileInputStream(filename);
            byte[] fileHeader = new byte[4];
            int bytesRead = fis.read(fileHeader);
            fis.close();

            if (bytesRead < 4) {
                System.out.println("File is too short to determine type.");
                return;
            }

            String fileType = getFileType(fileHeader);

            System.out.println("Filename: " + filename);
            StringBuilder hexHeaderPrint = new StringBuilder();
            hexHeaderPrint.append("File Header(Hex): [");
            for (byte b : fileHeader) {
                hexHeaderPrint.append(String.format("%02x", b));
                hexHeaderPrint.append(", ");
            }
            hexHeaderPrint.append("]");
            System.out.println(hexHeaderPrint);
            System.out.println("File Type: " + fileType);

        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }

    private static String getFileType(byte[] fileHeader) {
        StringBuilder hexHeader = new StringBuilder();
        for (byte b : fileHeader) {
            hexHeader.append(String.format("%02x", b));
        }

        return switch (hexHeader.toString()) {
            case "89504e47" -> "png";
            case "504b0304" -> "zip or jar";
            case "cafebabe" -> "class";
            default -> "unknown";
        };
    }
}