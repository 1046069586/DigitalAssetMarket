package com.example.back_end.service;

import java.io.IOException;

public interface OrderService {

    void buy(String username, String id) throws IOException;
}
