package glints.test.response;

import java.util.List;

public class ListData {
    Integer page;
    Integer per_page;
    Integer total;
    Integer total_pages;
    List<MainData> data;
    Ad ad;

    public Integer getPage() {
        return page;
    }

    public Integer getPer_page() {
        return per_page;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getTotal_pages() {
        return total_pages;
    }

    public List<MainData> getData() {
        return data;
    }

    public Ad getAd() {
        return ad;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setPer_page(Integer per_page) {
        this.per_page = per_page;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public void setTotal_pages(Integer total_pages) {
        this.total_pages = total_pages;
    }

    public void setData(List<MainData> data) {
        this.data = data;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }
}
