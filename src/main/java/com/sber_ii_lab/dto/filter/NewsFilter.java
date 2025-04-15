package com.sber_ii_lab.dto.filter;

import com.sber_ii_lab.enums.NewsType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NewsFilter {
    private NewsType newsType;
    private List<Long> tagIds;
}