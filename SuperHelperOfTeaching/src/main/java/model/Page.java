package model;

/**
 * @Description:分页操作封装
 * @Author:Anan
 * @Date:2019/8/11
 */
public class Page {
    private Integer start;//开始索引
    private Integer currentPage;//当前页面
    private Integer rows;//显示行数

    public Page(Integer currentPage, Integer rows) {
        this.currentPage = currentPage;
        this.rows = rows;
        this.start = (currentPage-1)*rows;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }
}
