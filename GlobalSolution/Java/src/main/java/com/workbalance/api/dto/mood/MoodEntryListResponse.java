package com.workbalance.api.dto.mood;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoodEntryListResponse {

    private List<MoodEntryResponse> items;
    private Long total;
}
