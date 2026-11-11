package com.antifraud.common;

import lombok.Data;
import java.util.List;

@Data
public class PageResult<T> {

    private List<T> records;
    private long total;
    private long pages;
    private long current;
    private long size;

    public PageResult() {}

    public PageResult(List<T> records, long total, long pages, long current, long size) {
        this.records = records;
        this.total = total;
        this.pages = pages;
        this.current = current;
        this.size = size;
    }
}
