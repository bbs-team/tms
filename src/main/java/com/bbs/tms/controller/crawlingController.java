package com.bbs.tms.controller;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.bbs.tms.entity.Searching;
import com.bbs.tms.entity.Video;
import com.bbs.tms.repository.SearchingRepository;
import com.bbs.tms.repository.VideoRepository;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/check")
public class crawlingController {

  @Autowired
  private VideoRepository videoRepo;

  @Autowired
  private SearchingRepository searchingRepo;

  @Scheduled(cron = "10 * * * * *")
  public void crawling() {

    try {

      Iterable<Searching> searchingList = searchingRepo.findAll();

      for(Searching query : searchingList){
        String url = "https://x.ibe.kr/bbs/search.php?url=https%3A%2F%2Fx.ibe.kr%2Fbbs%2Fsearch.php&stx=" + "런닝맨 720p NEXT"; //query.getQuery();

        if(url.indexOf("https://") >= 0){
          setSSL();
        }

        //토렌트왈 connection
        Document doc = Jsoup.connect(url).get();
        Elements Eles = doc.select("div[class=media-heading]");

        for(Element ele : Eles){
          //magent 추출을 위한 jsoup select
          Elements elementMagnet = ele.select("a");
          Elements elementTitle = ele.select("a > b");
          String title = elementTitle.text();
          String fullmagnet = elementMagnet.attr("onClick");

          //magnet정보만 뽑기
          String magnet = magnetString(fullmagnet);
          if(magnet.length() < 25) continue;
          System.out.println("title: " + title);
          System.out.println("magnet: " + magnet);
          
          String shellString = "transmission-remote -a \"" + magnet + "\" -w /media/hdd/complete-torrent/" + query.getKind() + "/" + query.getName();
          System.out.println(shellString);

          int exitcode = shellCommand(shellString);

          if(exitcode == 1){
            System.out.println("Shell command Error.");
          }else{
            //검색한 list's date 추출
            int Date = catchDate(title);
            if(Date == -1) continue;
            
            if(Date > query.getDate()){
              //searching 날짜 최신화
              query.setDate(Date);
              searchingRepo.save(query);

              //다운받은 항목 추가
              addVideo(title, query.getKind());
            }else{
              System.out.println("날짜가 더 빠르므로 넘어갑니다.");
            }
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (KeyManagementException e) {
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //마그넷 추출 및 생성
  public String magnetString(String listName) {
    
    int start = listName.indexOf("\'")+1;
    int last = listName.lastIndexOf("\'");
    String magnet = listName.substring(start, last);

    return magnet;

  }

  //다운받은 파일 DB에 저장
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

  //다운받는 파일 날짜 확인
  public int catchDate(String title) {

    String[] word = title.split("\\.", 4);
    int Date = -1;

    if(word.length < 3) return -1;
    else if(word[2].length() == 6){
      Date = Integer.parseInt(word[2]);
      return Date;
    }else if(word[3].length() == 6){
      Date = Integer.parseInt(word[2]);
      return Date;
    }
    return -1;
  }

  //SSL 인증
  public static void setSSL() throws NoSuchAlgorithmException, KeyManagementException {
    TrustManager[] trustAllCerts = new TrustManager[] { 
      new X509TrustManager(){

        @Override
        public X509Certificate[] getAcceptedIssuers() {
          return null;
        }
      
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
      
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
      }
    }; 
    SSLContext sc = SSLContext.getInstance("SSL"); 
    sc.init(null, trustAllCerts, new SecureRandom()); 
    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() { 
      @Override public boolean verify(String hostname, SSLSession session) { 
        return true; 
      } 
    }); 
  HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory()); 

  }

  public int shellCommand(String shell){
    
    int exitCode = 1;

    DefaultExecutor executor = new DefaultExecutor();

    try {

      CommandLine cmdLine = CommandLine.parse(shell);

      exitCode = executor.execute(cmdLine);

    } catch (Exception e) {
      e.printStackTrace();
    }



    return exitCode;
  }

}