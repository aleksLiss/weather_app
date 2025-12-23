package com.weather.app.controller;

import com.weather.app.service.IndexPageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class IndexControllerTest {
    @Mock
    private IndexPageService indexPageService;
    @InjectMocks
    private IndexController indexController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
    }

    @Test
    void whenGetIndexPageAndAuthenticateThenOk() throws Exception {
        when(indexPageService.isPopulateIndexModel(any(), any())).thenReturn(true);
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetIndexPageAndAuthenticateThenRedirectToSignUp() throws Exception {
        when(indexPageService.isPopulateIndexModel(any(), any())).thenReturn(false);
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection());
    }
}
