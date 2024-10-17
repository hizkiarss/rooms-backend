package com.rooms.rooms.peakSeason.controller;

import com.rooms.rooms.peakSeason.dto.ChangePriceForPeakSeasonDto;
import com.rooms.rooms.peakSeason.dto.UpdatePeakSeasonRequestDto;
import com.rooms.rooms.peakSeason.entity.PeakSeason;
import com.rooms.rooms.peakSeason.service.PeakSeasonService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PeakSeasonResolver {
    private final PeakSeasonService peakSeasonService;

    public PeakSeasonResolver(PeakSeasonService peakSeasonService) {
        this.peakSeasonService = peakSeasonService;
    }

    @PreAuthorize("hasAuthority('SCOPE_TENANT')")
    @MutationMapping(value = "changePriceForPeakSeason")
    public PeakSeason changePriceForPeakSeason(@Argument("input") ChangePriceForPeakSeasonDto dto) {
        return peakSeasonService.changePriceForPeakSeason(dto);
    }

    @QueryMapping(value = "getPeakSeasonsByPropertyId")
    public List<PeakSeason> getPeakSeasonsByPropertyId(@Argument Long propertyId) {
        return peakSeasonService.getPeakSeasonByPropertyId(propertyId);
    }

    @PreAuthorize("hasAuthority('SCOPE_TENANT')")
    @MutationMapping(value = "deletePeakSeason")
    public String deletePeakSeason(@Argument Long peakSeasonId) {
        peakSeasonService.deletePeakSeason(peakSeasonId);
        return "Peak season deleted";
    }

    @PreAuthorize("hasAuthority('SCOPE_TENANT')")
    @MutationMapping(value = "updatePeakSeason")
    public PeakSeason updatePeakSeason(@Argument("input") UpdatePeakSeasonRequestDto dto) {
        return peakSeasonService.updatePeakSeason(dto);
    }
}
