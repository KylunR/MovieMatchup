import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class ConvertTSVtoCSV {

    public static void main(String[] args) throws IOException {

        // Path to input file        
        String tsvPath = "C:\\Users\\kylun\\Downloads\\basics.tsv";

        // Path to output file
        String csvPath = "C:\\Users\\kylun\\Downloads\\basics.csv";


        convert(tsvPath, csvPath);
    }

    private static void convert(String tsvPath, String csvPath) throws IOException {

        StringTokenizer tokenizer;
        try (BufferedReader br = new BufferedReader(new FileReader(tsvPath));
            PrintWriter writer = new PrintWriter(new FileWriter(csvPath));) {
                

                int i = 0;

                for (String line; (line = br.readLine()) != null; ) {

                    if (line.contains("tvEpisode") || line.contains("tvMiniSeries") || line.contains("tvSeries") ||
                        line.contains("tvShort") || line.contains("tvSpecial") || line.contains("video") || 
                        line.contains("videoGame") || line.contains("short")) {
                        continue;
                    }
                    
                    i++;

                    if (i % 10000 == 0) {
                        System.out.println("Completed: " + i);
                    }

                    tokenizer = new StringTokenizer(line, "\t");
                    
                    String csvLine = "";

                    String token;

                    while (tokenizer.hasMoreTokens()) {
                        //token = tokenizer.nextToken().replaceAll("\"", "'");
                        //csvLine += "\"" + token + "\",";
                        token = tokenizer.nextToken().replaceAll(",", "");
                        csvLine += token + ",";
                    }

                    if (csvLine.endsWith(",")) {
                        csvLine = csvLine.substring(0, csvLine.length() - 1);
                    }

                    writer.write(csvLine + "\n");
                    //writer.write(csvLine);
                }
            }
    }
}