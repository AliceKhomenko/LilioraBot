package telegram.bot;


import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Random;


public class FindImage {

    File image = null;
    Elements links;
    String googleUrl = "https://www.google.com.ua/search?tbm=isch&q=";
    ArrayList<String> results = new ArrayList<>();

    public File find(String stringForSearch) throws IOException {
        results.clear();
        String google = googleUrl + stringForSearch;
        System.out.println(google);

        String imageUrl;

       links=getResultList(google);
        if (links.size() == 0) return null;


        int i = 0;

        Boolean isFind = false;


        do {


            imageUrl = new JSONObject( links.get(i).ownText()).get("ou").toString();
            if (results.contains(imageUrl)) {
                i++;
                continue;
            } else
                results.add(imageUrl);
            System.out.println(imageUrl);

            try {
                isFind = true;
                image = downloadFile(imageUrl);
            } catch (Exception e) {
                isFind = false;

                e.printStackTrace();
            } finally {
                i++;
            }
            if (i > 20) return null;

        }
        while (!((imageUrl.contains(".bmp") || imageUrl.contains(".jpg") || imageUrl.contains(".jpeg") || imageUrl.contains(".png")) && (isFind == true)));
        //       while (!"jpg".equals(s1) && !"png".equals(s1) && !"bmp".equals(s1));


        return image;
    }

    public void removeTempFile() {
        image.delete();


    }

    public File findNext(String stringForSearch) {
        results.clear();
        String google = googleUrl + stringForSearch;
        System.out.println(google);
        String imageUrl;
        Boolean isFind = false;

        links=getResultList(google);
        if (links.size() == 0) return null;
Random rand = new Random();
        do {
            int i=rand.nextInt(30);


            imageUrl = new JSONObject( links.get(i).ownText()).get("ou").toString();
            if (results.contains(imageUrl)) {
                i++;
                continue;
            } else
                results.add(imageUrl);
            System.out.println(imageUrl);

            try {
                isFind = true;
                image = downloadFile(imageUrl);
            } catch (Exception e) {
                isFind = false;

                e.printStackTrace();
            } finally {
                i++;
            }


        }
        while (!((imageUrl.contains(".bmp") || imageUrl.contains(".jpg") || imageUrl.contains(".jpeg") || imageUrl.contains(".png")) && (isFind == true)));
//        while(!"jpg".equals(s1)&&!"png".equals(s1)&&!"bmp".equals(s1));


        return image;
    }

    public String getFileName(String s) {
        if (s.contains(".jpg")) return "temp.jpg";
        if (s.contains(".jpeg")) return "temp.jpeg";
        if (s.contains(".bmp")) return "temp.bmp";
        if (s.contains(".png")) return "temp.png";
        return null;
    }

    public File downloadFile(String imageUrl) throws Exception {
        URLConnection con = new URL(imageUrl).openConnection();
        con.setConnectTimeout(1000);
        con.setReadTimeout(1000);
        InputStream in = con.getInputStream();
        String fileName = getFileName(imageUrl);

        Files.copy(in, Paths.get(fileName), StandardCopyOption.REPLACE_EXISTING);
        return new File(fileName);
    }

    public Elements getResultList(String google){

        Document doc = null;
        try {
            doc = Jsoup.connect(google)
                    .timeout(3000)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        links = doc.select("div.rg_meta.notranslate");
        return links;
    }
}

