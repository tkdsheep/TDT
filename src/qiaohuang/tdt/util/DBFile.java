package qiaohuang.tdt.util;

import java.util.Date;

/**
 * 
 * @author xuexianwu
 *
 */
public class DBFile {
  private String url;
  private String keywords;
  private String description;
  private String title;
  private String time;
  private String content;
  private String author;
  private String category;
  private String site;
  private String tag;
  private String source;
  private String table;
  private String summ;
  private String imgSrc;
  private int forward;
  private int read;
  private int comment;
  private int applaud;
  private int deep;
  private Date timeDate;
  

  public String getSumm() {
    return summ;
  }

  public void setSumm(String summ) {
    this.summ = summ;
  }

  public int getDeep() {
    return deep;
  }

  public void setDeep(int deep) {
    this.deep = deep;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getSite() {
    return site;
  }

  public void setSite(String site) {
    this.site = site;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public int getForward() {
    return forward;
  }

  public void setForward(int forward) {
    this.forward = forward;
  }

  public int getRead() {
    return read;
  }

  public void setRead(int read) {
    this.read = read;
  }

  public int getComment() {
    return comment;
  }

  public void setComment(int comment) {
    this.comment = comment;
  }

  public int getApplaud() {
    return applaud;
  }

  public void setApplaud(int applaud) {
    this.applaud = applaud;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public String getTable() {
    return table;
  }

  public void setTable(String table) {
    this.table = table;
  }

  
  public Date getTimeDate() {
    return timeDate;
  }

  public void setTimeDate(Date timeDate) {
    this.timeDate = timeDate;
  }

  public String getImgSrc() {
    return imgSrc;
  }

  public void setImgSrc(String imgSrc) {
    this.imgSrc = imgSrc;
  }

}
