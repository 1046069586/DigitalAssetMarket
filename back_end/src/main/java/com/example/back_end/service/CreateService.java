package com.example.back_end.service;

import java.io.IOException;

import com.example.back_end.entity.CreateForm;

public interface CreateService {

    void createNFT(CreateForm createForm) throws IOException;
}
