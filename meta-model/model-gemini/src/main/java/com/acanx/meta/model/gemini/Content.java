package com.acanx.meta.model.gemini;

import java.util.List;

/**
 * Content  使用Jackson进行序列化/反序列化 :cite[2]:cite[5]
 *
 * @author ACANX
 * @since 20251008
 */
public class Content {
    private List<Part> parts;

    private String role;

    public Content() {
    }

    public Content(List<Part> parts) {
        this.parts = parts;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }
}
