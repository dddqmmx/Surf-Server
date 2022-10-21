package com.dddqmmx.surf.server;

import com.dddqmmx.surf.server.mapper.RelationMapper;
import com.dddqmmx.surf.server.service.RelationService;
import com.dddqmmx.surf.server.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SurfServerApplicationTests {

    @Autowired
    private RelationService userService;

    @Test
    void contextLoads() {
    }

}
