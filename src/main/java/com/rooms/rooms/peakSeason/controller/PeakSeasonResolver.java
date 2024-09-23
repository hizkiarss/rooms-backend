package com.rooms.rooms.peakSeason.controller;

import com.rooms.rooms.peakSeason.dto.ChangePriceForPeakSeasonDto;
import com.rooms.rooms.peakSeason.entity.PeakSeason;
import com.rooms.rooms.peakSeason.service.PeakSeasonService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class PeakSeasonResolver {
    private final PeakSeasonService peakSeasonService;

    public PeakSeasonResolver(PeakSeasonService peakSeasonService) {
        this.peakSeasonService = peakSeasonService;
    }

    @MutationMapping(value = "changePriceForPeakSeason")
    public PeakSeason changePriceForPeakSeason(@Argument("input") ChangePriceForPeakSeasonDto dto) {
        return peakSeasonService.changePriceForPeakSeason(dto);
    }
}
