package model;

public class TypeCount {

    public TypeCount(Integer count, String type) {
        this.count = count;
        this.type = type;
    }

    private Integer count;
    private String type;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



}
