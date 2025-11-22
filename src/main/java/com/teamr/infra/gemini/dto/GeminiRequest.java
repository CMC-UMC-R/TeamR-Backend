package com.teamr.infra.gemini.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GeminiRequest {
    private final List<Content> contents;

    public static GeminiRequest of(String prompt) {
        Content content = Content.of(prompt);
        return new GeminiRequest(List.of(content));
    }

    public static GeminiRequest ofWithImage(String prompt, String imageBase64) {
        Content content = Content.ofWithImage(prompt, imageBase64);
        return new GeminiRequest(List.of(content));
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Content {
        private final List<Part> parts;

        public static Content of(String text) {
            Part part = Part.ofText(text);
            return new Content(List.of(part));
        }

        public static Content ofWithImage(String text, String imageBase64) {
            List<Part> parts = new ArrayList<>();
            parts.add(Part.ofText(text));
            parts.add(Part.ofImage(imageBase64));
            return new Content(parts);
        }
    }

    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Part {
        private final String text;
        private final InlineData inlineData;

        public static Part ofText(String text) {
            return new Part(text, null);
        }

        public static Part ofImage(String base64Data) {
            InlineData inlineData = new InlineData("image/jpeg", base64Data);
            return new Part(null, inlineData);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class InlineData {
        private final String mimeType;
        private final String data;
    }
}

