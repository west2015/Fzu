package com.west2.main.entity;

public class BookEntity {
	private String name;
	// 检索部分
	private String number;
	private String url;
	private String author;
	private String publisher;
	private String id;
	private String year;
	private String place;  //索书号
	private int store;  //馆藏数
	private int cntAmount; //可借数
	private String appointment_id; //预约码
	// 已借图书
	private String barCode;	// 条形码，用于续借
	private String collection; // 旗山校区
	private String returnDate;	// 归还日期
	
	public String getAppointment_id() {
		return appointment_id;
	}
	public void setAppointment_id(String appointment_id) {
		this.appointment_id = appointment_id;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public int getStore() {
		return store;
	}
	public void setStore(int store) {
		this.store = store;
	}
	public int getCntAmount() {
		return cntAmount;
	}
	public void setCntAmount(int cntAmount) {
		this.cntAmount = cntAmount;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getCollection() {
		return collection;
	}
	public void setCollection(String collection) {
		this.collection = collection;
	}
	public String getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}
	
}
