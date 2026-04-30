package com.acanx.meta.base;

import java.util.List;

/**
 * KeysetPage
 *
 * @author ACANX
 * @since 20260428
 */
public class KeysetPage<T> implements Page{

    private List<T> list;

    /**
     *  分页大小
     */
    private int ps;

    /**
     *  排序字符串
     */
    private String ordStr;


    public KeysetPage() {
    }

    public KeysetPage(int ps, String ordStr) {
        this.ps = ps;
        this.ordStr = ordStr;
    }

    public KeysetPage(int ps, String ordStr, List<T> list) {
        this.ps = ps;
        this.ordStr = ordStr;
        this.list = list;
    }


    @Override
    public int getPs() {
        return ps;
    }

    public void setPs(int ps) {
        this.ps = ps;
    }

    public List<T> getList() {
        return list;
    }
    public void setList(List<T> list) {
        this.list = list;
    }
    public String getOrdStr() {
        return ordStr;
    }
    public void setOrdStr(String ordStr) {
        this.ordStr = ordStr;
    }


    @Override
    public String toString() {
        return "KeysetPage{" +
                "ps=" + ps +
                ", ordStr='" + ordStr + '\'' +
                ", rows=" + list +
                '}';
    }
}
