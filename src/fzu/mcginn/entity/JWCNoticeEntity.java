package fzu.mcginn.entity;

public class JWCNoticeEntity {
	
	private String url;
	private String title;
	private String time;
	private boolean isRed;
	private String detailHtml;
	public String getDetailHtml() {
		return detailHtml;
	}
	public void setDetailHtml(String detailHtml) {
		this.detailHtml = detailHtml;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	public boolean isRed() {
		return isRed;
	}
	public void setRed(boolean isRed) {
		this.isRed = isRed;
	}
	
	
}
