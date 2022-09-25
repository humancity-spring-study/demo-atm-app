package com.example.atmdemo.service.dtos;

public record EmailParamDto(String receiverAddress, String title, String content) {

    @Override
    public String receiverAddress() {
        return receiverAddress;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public String content() {
        return content;
    }
}
