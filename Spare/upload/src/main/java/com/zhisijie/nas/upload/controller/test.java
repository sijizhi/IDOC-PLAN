package com.zhisijie.nas.upload.controller;

import java.util.List;

/**
 * @Author Sijie.Zhi
 * @title: test
 * @projectName upload
 * @description: TODO
 * @date 2019/4/14 11:58
 */
public class test {
    private String title;
    private String subtitle;
    private List<test> children;

    public test(String title, String subtitle, List<test> children) {
        this.title = title;
        this.subtitle = subtitle;
        this.children = children;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public List<test> getChildren() {
        return children;
    }

    public void setChildren(List<test> children) {
        this.children = children;
    }
}
