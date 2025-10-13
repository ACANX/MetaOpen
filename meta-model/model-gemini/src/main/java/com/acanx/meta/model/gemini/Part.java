package com.acanx.meta.model.gemini;

/**
 * Part
 *
 * @author ACANX
 * @since 20251008
 */
public class Part {
    private String text;

    public Part() {
    }

    public Part(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
