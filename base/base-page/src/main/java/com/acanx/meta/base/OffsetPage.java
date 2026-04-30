package com.acanx.meta.base;

import java.util.List;

/**
 * OffsetPage
 *
 * 基于偏移量的分页对象
 *
 * @author ACANX
 * @since 0.1.5
 */
public class OffsetPage<T> implements Page{

    /**
     *  分页上的数据列表
     */
    private List<T> list;

    /**
     *  分页大小
     */
    private int ps;

    /**
     *  当前（查询）页码
     */
    private int pn;

    /**
     *  总记录数
     */
    private int total;



    public OffsetPage() {
        this.pn = 1;
        this.ps = 10;
        this.total = 0;
    }

    public OffsetPage(int ps, int pn) {
        this.ps = ps;
        this.pn = pn;
    }

    public OffsetPage(int pn, int ps, List<T> rows) {
        this.ps = ps;
        this.pn = pn;
        this.total = ps;
        this.list = rows;
    }

    public OffsetPage(int pn, int ps, int total, List<T> rows) {
        this.ps = ps;
        this.pn = pn;
        this.total = total;
        this.list = rows;
    }

    @Override
    public int getPs() {
        return ps;
    }

    public void setPs(int ps) {
        this.ps = ps;
    }

    public int getPn() {
        return pn;
    }

    public void setPn(int pn) {
        this.pn = pn;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    /**
     *  总页数 pageTotal
     * @return 总页数
     */
    public int getPt() {
        if (this.total <= 0 || this.ps <= 0) {
            return 0;
        }
        return (this.total + this.ps - 1) / this.ps;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("OffsetPage{");
        sb.append(", ps=").append(ps);
        sb.append(", pn=").append(pn);
        sb.append(", total=").append(total);
        sb.append(", rows=").append(list);
        sb.append('}');
        return sb.toString();
    }
}
