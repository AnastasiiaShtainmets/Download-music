package ru.demo.download;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

    private static final String IN_FILE_TXT = "src\\ru\\demo\\download\\inFile1.txt";
    private static final String OUT_FILE_TXT = "src\\ru\\demo\\download\\outFile1.txt";
    private static final String PATH_TO_MUSIC = "src\\music";

    private static final String IN_FILE_TXT2 = "src\\ru\\demo\\download\\inFile2.txt";

    private static final String OUT_FILE_TXT2 = "src\\ru\\demo\\download\\outFile2.txt";

    private static final String PATH_TO_PICTURE = "src\\picture";

    public static void main(String[] args) {
            String Url;
            try (BufferedReader inFile = new BufferedReader(new FileReader(IN_FILE_TXT));
                 BufferedWriter outFile = new BufferedWriter(new FileWriter(OUT_FILE_TXT))) {
                while ((Url = inFile.readLine()) != null) {
                    URL url = new URL(Url);

                    String result;
                    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                        result = bufferedReader.lines().collect(Collectors.joining("\n"));
                    }

                    Pattern email_pattern = Pattern.compile("\\/\\/mp3uks.ru\\/mp3\\/files\\/[\\w./\\-]+");
                    Matcher matcher = email_pattern.matcher(result);
                    int i = 1;
                    while (matcher.find() && i <= 5) {
                        outFile.write("https:" + matcher.group() + "\r\n");
                        i++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (BufferedReader musicFile = new BufferedReader(new FileReader(OUT_FILE_TXT))) {
                String music;
                int count = 1;
                try {
                    while ((music = musicFile.readLine()) != null) {
                        downloadUsingNIO(music, PATH_TO_MUSIC + String.valueOf(count) + ".mp3");
                        count++;
                    }
                    System.out.println("Работает");
                    System.out.println();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Ошибка");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        try (BufferedReader inFile = new BufferedReader(new FileReader(IN_FILE_TXT2));
             BufferedWriter outFile = new BufferedWriter(new FileWriter(OUT_FILE_TXT2))) {
            while ((Url = inFile.readLine()) != null) {
                URL url = new URL(Url);

                String result2;
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                    result2 = bufferedReader.lines().collect(Collectors.joining("\n"));
                }

                Pattern email_pattern = Pattern.compile("\\/\\/static.wixstatic.com\\/media\\/632d61_[\\w./\\-\\$\\?\\_\\%\\~]+");
                Matcher matcher = email_pattern.matcher(result2);
                int i = 1;
                while (matcher.find() && i <= 3) {
                    outFile.write("https:" + matcher.group() + "\r\n");
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader pictureFile = new BufferedReader(new FileReader(OUT_FILE_TXT2))) {
            String picture;
            int count = 1;
            try {
                while ((picture = pictureFile.readLine()) != null) {
                    downloadUsingNIO(picture, PATH_TO_PICTURE + String.valueOf(count) + ".jpg");
                    count++;
                }
                System.out.println("Работает");
                System.out.println();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Ошибка");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void downloadUsingNIO(String strUrl, String file) throws IOException {
        URL url = new URL(strUrl);
        ReadableByteChannel byteChannel = Channels.newChannel(url.openStream());
        FileOutputStream stream = new FileOutputStream(file);
        stream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
        stream.close();
        byteChannel.close();
    }
}
