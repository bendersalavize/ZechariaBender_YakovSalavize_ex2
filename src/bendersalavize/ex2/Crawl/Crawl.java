package bendersalavize.ex2.Crawl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Crawl {
    public static void main(String[] args) {
        URLCheckerFactory urlCheckerFactory = new URLCheckerFactory();
        if (args.length == 4) {
            int poolSize = Integer.valueOf(args[0]);
            int delay = Math.abs(Integer.valueOf(args[1]));
            int attempts = Integer.valueOf(args[2]);
            String filename = args[3];
            // get instance of ImageURLChecker from urlCheckerFactory
            URLChecker urlChecker = urlCheckerFactory.getChecker("image");
            DatabaseManager dbm = new DatabaseManager();
            try {
                BufferedReader urlReader = new BufferedReader(new FileReader(filename));
                String url;
                dbm.initDB();
                ExecutorService pool = Executors.newFixedThreadPool(poolSize);
                // run each url task on separate thread
                while ((url = urlReader.readLine()) != null)
                    pool.execute(new URLAnalyzeInsertTask(url, urlChecker, delay, attempts, dbm));
                pool.shutdown();
                // wait for all threads to finish, and then output performance log
                pool.awaitTermination(1, TimeUnit.HOURS);
                for (int i = 0; i < URLAnalyzeInsertTask.getPerformanceLog().size(); i++) {
                    System.out.println(URLAnalyzeInsertTask.getPerformanceLog().get(i));
                }
                urlReader.close();
                dbm.printDB();
            } catch (FileNotFoundException e) {
                System.err.println("file \"" + filename + "\" not found");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            finally {
                dbm.closeDB();
            }
        }
        else {
            System.out.println("--------------------------------------------------------------------\n" +
                    "\n" +
                    "    Crawl\n" +
                    "\n" +
                    "    By:\n" +
                    "    Zecharia bender ID: 320826118\n" +
                    "    Yakov Salavize ID: 203810239\n" +
                    "\n" +
                    "    Date: May 1 2019\n" +
                    "\n" +
                    "--------------------------------------------------------------------\n" +
                    "    \n" +
                    "    This program is a simple web crawler that scans URLs of images,\n" +
                    "    records thread performance, and stores them in a database\n" +
                    "    (only the URL, not the image itself).\n" +
                    "\n" +
                    "    Crawl receives 4 mandatory arguments:\n" +
                    "        1. first a pool size (positive non zero number, see below),\n" +
                    "        2. second a delay for retries (positive non zero milliseconds),\n" +
                    "        3. third a number of retries,\n" +
                    "        4. fourth a file name.\n" +
                    "\n" +
                    "--------------------------------------------------------------------");
        }
    }
}
