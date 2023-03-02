package com.dddqmmx.surf.server.pojo;

import java.io.Serializable;

public class SurfFile implements Serializable {
    private Integer id;
    private String md5;
    private long length;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "File{" +
                "id=" + id +
                ", md5='" + md5 + '\'' +
                ", length=" + length +
                '}';
    }
}
