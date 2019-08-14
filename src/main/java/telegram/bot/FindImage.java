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
import java.nio.file.Files;
import java.nio.file.Paths;


public class FindImage {
    File image = null;
    public File find(String stringForSearch) throws IOException {
        String google = "https://www.google.com.ua/search?tbm=isch&q="+stringForSearch;
        System.out.println(google);

        String imageUrl;

        Document doc = Jsoup.connect(google)
                .timeout(3000)
                .get();
        Elements links = doc.select("div.rg_meta.notranslate");
        if (links.size()==0) return null;


        int i=0;
        String fileName;
        String s1;



do {

  String  fileObj=links.get(i).ownText();

    JSONObject jsonObj = new JSONObject(fileObj);

    imageUrl = jsonObj.get("ou").toString();
    System.out.println(imageUrl);
    String[] sArray = imageUrl.split("/");
    fileName = sArray[sArray.length - 1];

    s1 = fileName.substring(fileName.length()-3);


    i++;
}
while(!"jpg".equals(s1)&&!"png".equals(s1)&&!"bmp".equals(s1));


        try(InputStream in = new URL(imageUrl).openStream()){
            Files.copy(in, Paths.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        image = new File(fileName);
return image;
    }
    public void removeTempFile(){
      image.delete();


    }
}

