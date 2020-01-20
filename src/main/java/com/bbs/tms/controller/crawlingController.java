package com.bbs.tms.controller;

import java.io.IOException;

import com.bbs.tms.entity.Searching;
import com.bbs.tms.entity.Video;
import com.bbs.tms.repository.SearchingRepository;
import com.bbs.tms.repository.VideoRepository;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class crawlingController {

  @Autowired
  private VideoRepository videoRepo;

  @Autowired
  private SearchingRepository searchingRepo;

  @Scheduled(cron = "10 * * * * *")
  public void test() {

    try {

      Iterable<Searching> searchingList = searchingRepo.findAll();

      for(Searching query : searchingList){
        String url = "https://torrentwal2.com/bbs/s.php?k=" + query.getQuery() + "&q=";

        Document doc = Jsoup.connect(url).get();

        Elements Eles = doc.select("tr[class=bg1]");

        for(Element ele : Eles){
          Elements elementMagnet = ele.select("td[class=num] > a");
          Elements elementTitle = ele.select("td[class=subject] > a[target=s]");
          String title = elementTitle.text();
          String fullmagnet = elementMagnet.attr("href");
          String magnet = magnetString(fullmagnet);
          
          int Date = catchDate(title);
          
          if(Date > query.getDate()){
            addVideo(title, query.getKind());
          }else{
            System.out.println("날짜가 더 빠르므로 넘어갑니다.");
          }

        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public String magnetString(String listName) {
    
    int start = listName.indexOf("\'")+1;
    int last = listName.lastIndexOf("\'");
    String magnet = "magnet:?xt=urn:btih:";
    magnet += listName.substring(start, last);

    return magnet;

  }

  public void addVideo(String title, String kind){

    if(!videoRepo.existsByName(title)){
      Video video = new Video();
      video.setKind(kind);
      video.setName(title);
      videoRepo.save(video);
      System.out.println("List에 존재하지 않아 magnet을 전송합니다.");
    }else{
      System.out.println("\'" + title + "\'가 존재하여 전송하지 않습니다.");
    }
  }

  public int catchDate(String title) {
    int firstP = title.indexOf(".");
    int secondP = title.indexOf(".", firstP+1)+1;
    int lastP = title.indexOf(".", secondP+1);
    
    String myDate = "";
    myDate += title.substring(secondP, lastP);

    int Date = Integer.parseInt(myDate);

    return Date;
  }

}