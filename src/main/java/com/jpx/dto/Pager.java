package com.jpx.dto;

import java.util.List;

/**
 * 分页
 */
public class Pager<T> {

    private int pageNow;

    private int pageCount;

    private int totalCount;

    private int pageSize;

    private List<T> data;

    public Pager() {
    }

    public Pager(int pageNow, int pageCount, int totalCount, int pageSize, List<T> data) {
        this.pageNow = pageNow;
        this.pageCount = pageCount;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.data = data;
    }

    public int getPageNow() {
        return pageNow;
    }

    public void setPageNow(int pageNow) {
        this.pageNow = pageNow;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
